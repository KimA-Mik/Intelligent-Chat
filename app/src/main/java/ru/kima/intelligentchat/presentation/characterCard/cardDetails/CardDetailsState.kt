package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard

@Immutable
data class CardDetailsState(
    val card: ImmutableCard = ImmutableCard(),
    val additionalSurfaces: AdditionalSurfaces = AdditionalSurfaces(),
    val tokensCount: TokensCount = TokensCount(),
    val selectedTabIndex: Int = 0,
    val switchesState: SwitchesState = SwitchesState()
) {
    data class TokensCount(
        val name: Int = 0,
        val description: Int = 0,
        val personality: Int = 0,
        val scenario: Int = 0,
        val firstMes: Int = 0,
        val mesExample: Int = 0,
        val systemPrompt: Int = 0,
        val postHistoryInstructions: Int = 0,
    ) {
        val totalTokens = name +
                description +
                personality +
                scenario +
                firstMes +
                mesExample +
                systemPrompt +
                postHistoryInstructions
    }

    data class AdditionalSurfaces(
        val showAltGreeting: Boolean = false,
        val deleteAltGreetingDialog: Boolean = false,
        val editableGreeting: Long = 0,
        val editableGreetingBuffer: String = String(),
    )

    data class SwitchesState(
        //History
        val description: Boolean = true,
        val firstMes: Boolean = true,
        val personality: Boolean = false,
        val scenario: Boolean = false,
        val mesExample: Boolean = false,

        //System
        val creator: Boolean = true,
        val characterVersion: Boolean = false,
        val creatorsNote: Boolean = false,
    )
}
