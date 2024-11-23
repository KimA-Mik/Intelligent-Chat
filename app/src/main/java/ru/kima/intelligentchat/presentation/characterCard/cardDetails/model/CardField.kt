package ru.kima.intelligentchat.presentation.characterCard.cardDetails.model

enum class CardField(val string: String) {
    Id("cardId"),
    Name("cardName"),
    Description("cardDescription"),
    Personality("cardPersonality"),
    Scenario("cardScenario"),
    FirstMes("cardFirstMes"),
    MesExample("cardMesExample"),
    CreatorNotes("cardCreatorNotes"),
    SystemPrompt("cardSystemPrompt"),
    PostHistoryInstructions("cardPostHistoryInstructions"),
    AlternateGreetings("cardAlternateGreetings"),
    Tags("cardTags"),
    Creator("cardCreator"),
    CharacterVersion("cardCharacterVersion")
}