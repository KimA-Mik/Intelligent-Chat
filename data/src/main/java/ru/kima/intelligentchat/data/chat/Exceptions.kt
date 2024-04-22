package ru.kima.intelligentchat.data.chat

abstract class ChatException : Exception()
class ChatNotFoundException : ChatException()