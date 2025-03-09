package ru.kima.intelligentchat.domain.messaging.generation.prompting

import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.ResolvedInstructMode
import ru.kima.intelligentchat.domain.messaging.generation.model.ResolvedInstructModeBudget
import ru.kima.intelligentchat.domain.tokenizer.Tokenizer

//private const val PROMPT_CONTENT =
//    "Avoid repetition, don't loop. Develop the plot slowly, always stay in character. Describe all actions in full, elaborate, explicit, graphic, and vivid detail. Mention all relevant sensory perceptions."
//private const val CHAT_BEGINNING = "### New Roleplay:"
//private const val MESSAGE_EXAMPLES_DELIMITER = "### New Roleplay:"
//private const val USER_MESSAGE_PREFIX = "### Instruction:"
//private const val ASSISTANT_MESSAGE_PREFIX = "### Response:"
//private const val LAST_ASSISTANT_MESSAGE_PREFIX =
//    "### Response (2 paragraphs, engaging, natural, authentic, descriptive, creative):"

private const val MESSAGE_EXAMPLES_DELIMITER_STRING = "<START>"

private data class PromptMessage(
    val senderType: SenderType,
    val body: String,
)

fun GenerationRequest.constructPrompt(templateResolver: TemplateResolver) = buildString {
    var tokenBudget = 0
    val appendToPrompt = fun(field: String): Boolean {
        if (field.isBlank()) return true

        val tokens = tokenizer.encode(field).size
        if (tokens + tokenBudget >= maxContextLength) return false
        tokenBudget += tokens
        append(field)

        return true
    }

    val story = templateResolver.constructFullTemplate(
        template = contextTemplate.storyString,
        inputData = TemplateResolver.FullInputData.construct(persona, card)
    )

    if (!appendToPrompt(story)) return@buildString

    //TODO: Handle last prefixes better
    val instructMode = resolveInstructMode(
        resolver = templateResolver,
        instructModeTemplate = instructModeTemplate,
        user = persona.name,
        char = card.name
    )
    val instructModeBudget = instructMode.estimateInstructModeBudget(tokenizer)

    val end = when (generateFor) {
        SenderType.Character -> instructMode.lastAssistantPrefix.ifBlank { "${card.name}: " }
        SenderType.Persona -> instructMode.lastUserPrefix.ifBlank { "${persona.name}: " }
    }
    val endTokens = tokenizer.encode(end).size
    if (tokenBudget + endTokens >= maxContextLength) return@buildString
    tokenBudget += endTokens

    val chatStart = "\n${contextTemplate.chatStart.ifBlank { "" }}\n"
    val chatStartTokens = tokenizer.encode(chatStart).size
    val promptMessages = getPromptMessages(chat.messages)

    val prepared: MutableList<PreparedMessage> = ArrayList(promptMessages.size)
    for (i in promptMessages.lastIndex downTo 0) {
        val message = promptMessages[i].prepare(
            instructMode = instructMode,
            budget = instructModeBudget,
            tokenizer = tokenizer
        )

        prepared.add(message)
    }

    prepared.startDifferentiatingMessages(generateFor, instructMode, instructModeBudget)
    val initialTokenPrice = prepared.totalTokensCount()

    val canFitExamples = initialTokenPrice + chatStartTokens + tokenBudget < maxContextLength
    if (canFitExamples) {
        extractExamples(
            plainExamples = card.mesExample,
            cardName = card.name, personaName = persona.name,
            availableBudget = maxContextLength - tokenBudget - chatStartTokens - initialTokenPrice,
            tokenizer = tokenizer,
            templateResolver = templateResolver,
            contextTemplate = contextTemplate,
            instructMode = instructMode
        ).forEach { append(it) }
    } else {
        prepared.stripMessagesToTokenCount(
            instructMode = instructMode,
            budget = instructModeBudget,
            requiredTokensCount = maxContextLength - tokenBudget - chatStartTokens
        )
    }

    append(chatStart)
    for (message in prepared.asReversed()) {
        append(message.fullText)
    }
    append(end)
}


private fun extractExamples(
    plainExamples: String,
    cardName: String,
    personaName: String,
    availableBudget: Int,
    tokenizer: Tokenizer,
    templateResolver: TemplateResolver,
    contextTemplate: ContextTemplate,
    instructMode: ResolvedInstructMode
): List<String> {
    val res = mutableListOf<String>()
    if (plainExamples.isBlank()) return res
    var tokensUsed = 0

    val examples = templateResolver.constructBasicTemplate(
        template = plainExamples,
        inputData = TemplateResolver.BasicInputData(personaName, cardName)
    ).split(MESSAGE_EXAMPLES_DELIMITER_STRING)

    for (example in examples) {
        val lines = example.lines()
        val body = buildString {
            append(contextTemplate.exampleSeparator)
            append("\n\n")
            for (line in lines) {
                if (line.startsWith(cardName)) {
                    append(instructMode.assistantMessagePrefix)
                    append('\n')
                } else if (line.startsWith(personaName)) {
                    append(instructMode.userMessagePostfix)
                    append('\n')
                }

                append(line)
//                append("\n\n")
                append('\n')
            }
        }
        val bodyTokens = tokenizer.encode(body).size
        if (bodyTokens + tokensUsed >= availableBudget) break
        tokensUsed += bodyTokens
        res.add(body)
    }
    return res
}

private fun getPromptMessages(
    messages: List<MessageWithSwipes>,
): List<PromptMessage> = messages.mapNotNull {
    if (it.selectedSwipeIndex > it.swipes.lastIndex) {
        return@mapNotNull null
    }

    PromptMessage(
        senderType = it.sender,
        body = it.swipes[it.selectedSwipeIndex].text,
    )
}

private fun PromptMessage.prepare(
    instructMode: ResolvedInstructMode,
    budget: ResolvedInstructModeBudget,
    tokenizer: Tokenizer
): PreparedMessage {
    val prefix: String
    val postfix: String
    val additionalPrice: Int
    when (senderType) {
        SenderType.Character -> {
            prefix = instructMode.assistantMessagePrefix
            postfix = instructMode.assistantMessagePostfix
            additionalPrice = budget.assistantMessagePrefix + budget.assistantMessagePostfix
        }

        SenderType.Persona -> {
            prefix = instructMode.userMessagePrefix
            postfix = instructMode.userMessagePostfix
            additionalPrice = budget.userMessagePrefix + budget.userMessagePostfix
        }
    }

    val bodyPrice = tokenizer.encode(body).size
    return PreparedMessage(
        senderType = senderType,
        body = body,
        bodyTokenPrice = bodyPrice,
        prefix = prefix,
        postfix = postfix,
        additionalTokenPrice = additionalPrice
    )
}


private fun resolveInstructMode(
    resolver: TemplateResolver,
    instructModeTemplate: InstructModeTemplate,
    user: String,
    char: String
): ResolvedInstructMode {
    val inputData = TemplateResolver.BasicInputData(
        user = user,
        char = char
    )
    return ResolvedInstructMode(
        userMessagePrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.userMessagePrefix,
                inputData = inputData
            ),
            name = user,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        userMessagePostfix = resolvePostfix(
            postfix = resolver.constructBasicTemplate(
                template = instructModeTemplate.userMessagePostfix,
                inputData = inputData
            ),
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        assistantMessagePrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.assistantMessagePrefix,
                inputData = inputData
            ),
            name = char,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        assistantMessagePostfix = resolvePostfix(
            postfix = resolver.constructBasicTemplate(
                template = instructModeTemplate.assistantMessagePostfix,
                inputData = inputData
            ),
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        firstAssistantPrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.firstAssistantPrefix,
                inputData = inputData
            ),
            name = char,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        lastAssistantPrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.lastAssistantPrefix,
                inputData = inputData
            ),
            name = char,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        firstUserPrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.firstUserPrefix,
                inputData = inputData
            ),
            name = user,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
        lastUserPrefix = resolvePrefix(
            prefix = resolver.constructBasicTemplate(
                template = instructModeTemplate.lastUserPrefix,
                inputData = inputData
            ),
            name = user,
            includeNamePolicy = instructModeTemplate.includeNamePolicy,
            wrapSequencesWithMewLines = instructModeTemplate.wrapSequencesWithNewLine
        ),
    )
}

private fun resolvePrefix(
    prefix: String,
    name: String,
    includeNamePolicy: IncludeNamePolicy,
    wrapSequencesWithMewLines: Boolean,
    last: Boolean = false
) = buildString(prefix.length + name.length + 3) {
    val blank = prefix.isBlank()
    if (!blank) {
        append(prefix)
        if (wrapSequencesWithMewLines) {
            append('\n')
        }
    } else if (last) {
        return@buildString
    }

    when (includeNamePolicy) {
        IncludeNamePolicy.ALWAYS -> {
            append(name)
            append(':')
            append(' ')
        }

        IncludeNamePolicy.NEVER -> {}

    }
}

private fun resolvePostfix(
    postfix: String,
    wrapSequencesWithMewLines: Boolean
) = buildString(postfix.length + 1) {
    if (wrapSequencesWithMewLines) {
        append('\n')
    }
    append(postfix)
}

private fun ResolvedInstructMode.estimateInstructModeBudget(tokenizer: Tokenizer) =
    ResolvedInstructModeBudget(
        userMessagePrefix = tokenizer.encode(userMessagePrefix).size,
        userMessagePostfix = tokenizer.encode(userMessagePostfix).size,
        assistantMessagePrefix = tokenizer.encode(assistantMessagePrefix).size,
        assistantMessagePostfix = tokenizer.encode(assistantMessagePostfix).size,
        firstAssistantPrefix = tokenizer.encode(firstAssistantPrefix).size,
        lastAssistantPrefix = tokenizer.encode(lastAssistantPrefix).size,
        firstUserPrefix = tokenizer.encode(firstUserPrefix).size,
        lastUserPrefix = tokenizer.encode(lastUserPrefix).size
    )