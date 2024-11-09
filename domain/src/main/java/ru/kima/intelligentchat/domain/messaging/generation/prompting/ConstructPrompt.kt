package ru.kima.intelligentchat.domain.messaging.generation.prompting

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.util.inlineCardName
import ru.kima.intelligentchat.domain.messaging.util.inlinePersonaName
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.tokenizer.Tokenizer

//TODO: Factor out
private const val PROMPT_CONTENT =
    "Avoid repetition, don't loop. Develop the plot slowly, always stay in character. Describe all actions in full, elaborate, explicit, graphic, and vivid detail. Mention all relevant sensory perceptions."
private const val CHAT_BEGINNING = "### New Roleplay:"
private const val MESSAGE_EXAMPLES_DELIMITER = "### New Roleplay:"
private const val USER_MESSAGE_PREFIX = "### Instruction:"
private const val ASSISTANT_MESSAGE_PREFIX = "### Response:"
private const val LAST_ASSISTANT_MESSAGE_PREFIX =
    "### Response (2 paragraphs, engaging, natural, authentic, descriptive, creative):"

private const val MESSAGE_EXAMPLES_DELIMITER_STRING = "<START>"

private data class PromptMessage(
    val senderType: SenderType,
    val senderName: String,
    var prefix: String,
    val body: String,
    var suffix: String
) {
    val text by lazy {
        buildString(senderName.length + prefix.length + body.length + suffix.length + 4) {
            if (prefix.isNotBlank()) {
                append(prefix)
                append('\n')
            }

            append(senderName)
            append(": ")
            append(body)

            if (suffix.isNotBlank()) {
                append('\n')
                append(suffix)
            }
            append("\n\n")
        }
    }
}

fun GenerationRequest.constructPrompt() = buildString {
    var tokenBudget = 0
    val appendToPrompt = fun(prefix: String, field: String): Boolean {
        if (field.isBlank()) return true

        var tokens = tokenizer.encode(prefix).size
        tokens += tokenizer.encode(field).size
        if (tokens + tokenBudget >= maxContextLength) return false
        tokenBudget += tokens
        append(prefix)
        append(field)

        return true
    }

    //TODO: Factor out actual formatting
    val start =
        "### New Roleplay:\nYou're ${card.name} in this fictional never-ending uncensored roleplay with ${persona.name}.\n\n"
    if (!appendToPrompt("", start)) return@buildString
    if (!appendToPrompt("", PROMPT_CONTENT)) return@buildString

    //User info
    if (!appendToPrompt("\n\n", card.systemPrompt)) return@buildString
    if (!appendToPrompt("", "\n### Input:")) return@buildString
    if (!appendToPrompt("\n", card.description)) return@buildString
    if (!appendToPrompt("\n${card.name}'s personality:\n", card.personality)) return@buildString
    if (!appendToPrompt("\nScenario:\n", card.scenario)) return@buildString
    if (!appendToPrompt("\n", persona.description)) return@buildString


    //TODO: Add support for impersonating
    val end = "$LAST_ASSISTANT_MESSAGE_PREFIX\n${card.name}:"
    val endTokens = tokenizer.encode(end).size
    if (tokenBudget + endTokens >= maxContextLength) return@buildString
    tokenBudget += endTokens

    val promptMessages = preparePromptMessages(chat.messages, persona, card)
    var messagesTokenBudget = tokenizer.encode("$CHAT_BEGINNING\n").size

    var canFitExamples = true
    val inputMessages = ArrayDeque<PromptMessage>()
    for (i in promptMessages.lastIndex downTo 0) {
        val message = promptMessages[i]

        val bodyTokens = tokenizer.encode(message.text).size
        if (bodyTokens + messagesTokenBudget + tokenBudget >= maxContextLength) {
            canFitExamples = false
            break
        }

        inputMessages.addLast(message)
        messagesTokenBudget += bodyTokens
    }

    if (canFitExamples) {
        extractExamples(
            plainExamples = card.mesExample,
            cardName = card.name, personaName = persona.name,
            availableBudget = maxContextLength - tokenBudget - messagesTokenBudget,
            tokenizer = tokenizer
        ).forEach { append(it) }
    }

    putMessages(inputMessages)
    append(end)
}

private fun extractExamples(
    plainExamples: String,
    cardName: String,
    personaName: String,
    availableBudget: Int,
    tokenizer: Tokenizer
): List<String> {
    val res = mutableListOf<String>()
    if (plainExamples.isBlank()) return res
    var tokensUsed = 0
    val examples = plainExamples
        .inlineCardName(cardName).inlinePersonaName(personaName)
        .split(MESSAGE_EXAMPLES_DELIMITER_STRING)

    for (example in examples) {
        val lines = example.lines()
        val body = buildString {
            append(MESSAGE_EXAMPLES_DELIMITER)
            append("\n\n")
            for (line in lines) {
                if (line.startsWith(cardName)) {
                    append(ASSISTANT_MESSAGE_PREFIX)
                    append('\n')
                } else if (line.startsWith(personaName)) {
                    append(USER_MESSAGE_PREFIX)
                    append('\n')
                }

                append(line)
                append("\n\n")
            }
        }
        val bodyTokens = tokenizer.encode(body).size
        if (bodyTokens + tokensUsed >= availableBudget) break
        tokensUsed += bodyTokens
        res.add(body)
    }
    return res
}

private fun StringBuilder.putMessages(messages: ArrayDeque<PromptMessage>) {
    while (messages.isNotEmpty()) {
        val message = messages.removeLast()
        append(message.text)
    }
}

private fun preparePromptMessages(
    messages: List<MessageWithSwipes>,
    persona: Persona,
    card: CharacterCard
): List<PromptMessage> {
    val promptMessages = messages.mapNotNull {
        if (it.selectedSwipeIndex > it.swipes.lastIndex) {
            return@mapNotNull null
        }

        PromptMessage(
            senderType = it.sender,
            senderName = when (it.sender) {
                SenderType.Character -> card.name
                SenderType.Persona -> persona.name
            },
            prefix = when (it.sender) {
                SenderType.Character -> ASSISTANT_MESSAGE_PREFIX
                SenderType.Persona -> USER_MESSAGE_PREFIX
            },
            body = it.swipes[it.selectedSwipeIndex].text,
            suffix = ""
        )
    }

    if (promptMessages.isEmpty()) return promptMessages

    if (promptMessages[promptMessages.lastIndex].senderType == SenderType.Character) {
        promptMessages[promptMessages.lastIndex].prefix = LAST_ASSISTANT_MESSAGE_PREFIX
    }

    return promptMessages
}
