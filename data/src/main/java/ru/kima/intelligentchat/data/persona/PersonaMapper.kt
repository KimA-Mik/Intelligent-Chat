package ru.kima.intelligentchat.data.persona

import ru.kima.intelligentchat.domain.persona.model.Persona

fun PersonaEntity.toPersona(): Persona {
    return Persona(
        id = id,
        name = name,
        description = description,
        imageName = imageFilePath,
        personaWordsCount = personaWordsCount,
        charactersWordsCount = charactersWordsCount,
        personaMessages = personaMessages,
        charactersMessages = charactersMessages,
        swipes = swipes
    )
}

fun Persona.toEntity(): PersonaEntity {
    return PersonaEntity(
        id = id,
        name = name,
        description = description,
        imageFilePath = imageName,
        personaWordsCount = personaWordsCount,
        charactersWordsCount = charactersWordsCount,
        personaMessages = personaMessages,
        charactersMessages = charactersMessages,
        swipes = swipes
    )
}

fun getPersonaAvatarFileName(id: Long) = "persona-$id.png"