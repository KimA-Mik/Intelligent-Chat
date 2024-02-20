package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.UserDetailsDto
import ru.kima.intelligentchat.domain.horde.model.UserInfo

fun UserDetailsDto.toUserInfo() =
    UserInfo(
        userName = username,
        id = id,
        kudos = kudos
    )