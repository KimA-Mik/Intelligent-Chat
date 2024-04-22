package ru.kima.intelligentchat.domain.chat

abstract class ChatException : Exception()
class ChatNotFoundException : ChatException()