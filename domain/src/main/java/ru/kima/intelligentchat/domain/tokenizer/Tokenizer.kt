package ru.kima.intelligentchat.domain.tokenizer

interface Tokenizer {

    fun encode(
        prompt: String,
        addBosToken: Boolean = true,
        addPrecedingSpace: Boolean = true
    ): IntArray

    enum class Type {
        LLama
    }
}