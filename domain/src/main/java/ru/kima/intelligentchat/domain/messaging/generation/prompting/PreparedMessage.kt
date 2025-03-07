package ru.kima.intelligentchat.domain.messaging.generation.prompting

import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.generation.model.ResolvedInstructMode
import ru.kima.intelligentchat.domain.messaging.generation.model.ResolvedInstructModeBudget


data class PreparedMessage(
    val senderType: SenderType,
    val body: String,
    val bodyTokenPrice: Int,
    var prefix: String,
    var postfix: String,
    var additionalTokenPrice: Int
) {
    val fullText
        get() = buildString(body.length + prefix.length + postfix.length) {
            append(prefix)
            append(body)
            append(postfix)
        }

    val fullTokenPrice
        get() = bodyTokenPrice + additionalTokenPrice

    fun makeFirst(
        instructMode: ResolvedInstructMode,
        budget: ResolvedInstructModeBudget
    ) {
        when (senderType) {
            SenderType.Character -> {
                prefix = instructMode.firstAssistantPrefix
                additionalTokenPrice = budget.firstAssistantPrefix + budget.assistantMessagePostfix
            }

            SenderType.Persona -> {
                prefix = instructMode.firstUserPrefix
                additionalTokenPrice = budget.firstUserPrefix + budget.userMessagePostfix
            }
        }
    }

    fun makeLast(
        instructMode: ResolvedInstructMode,
        budget: ResolvedInstructModeBudget
    ) {
        when (senderType) {
            SenderType.Character -> {
                prefix = instructMode.lastAssistantPrefix
                additionalTokenPrice = budget.lastAssistantPrefix + budget.assistantMessagePostfix
            }

            SenderType.Persona -> {
                prefix = instructMode.lastUserPrefix
                additionalTokenPrice = budget.lastUserPrefix + budget.userMessagePostfix
            }
        }
    }
}

fun List<PreparedMessage>.startDifferentiatingMessages(
    instructMode: ResolvedInstructMode,
    budget: ResolvedInstructModeBudget
) {
    if (isEmpty()) return
    first().makeLast(instructMode, budget)
    differentiateMessages(instructMode, budget)
}

fun List<PreparedMessage>.differentiateMessages(
    instructMode: ResolvedInstructMode,
    budget: ResolvedInstructModeBudget
) {
    if (isEmpty()) return
    val last = last()
    last.makeFirst(instructMode, budget)

    val next = indexOfLast { it.senderType != last.senderType }
    if (next > -1) {
        get(next).makeFirst(instructMode, budget)
    }
}

fun List<PreparedMessage>.totalTokensCount() =
    sumOf { it.fullTokenPrice }


fun MutableList<PreparedMessage>.stripMessagesToTokenCount(
    instructMode: ResolvedInstructMode,
    budget: ResolvedInstructModeBudget,
    requiredTokensCount: Int,
) {
    var currentTokensCount = totalTokensCount()
    while (isNotEmpty() && currentTokensCount > requiredTokensCount) {
        removeAt(lastIndex)
        differentiateMessages(instructMode, budget)
        currentTokensCount = totalTokensCount()
    }
}