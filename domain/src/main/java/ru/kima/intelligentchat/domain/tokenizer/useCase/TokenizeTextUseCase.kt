package ru.kima.intelligentchat.domain.tokenizer.useCase

import ru.kima.intelligentchat.domain.tokenizer.LlamaTokenizer

class TokenizeTextUseCase(
    private val tokenizer: LlamaTokenizer
) {
    operator fun invoke(text: String): IntArray {
        return tokenizer.encode(text)
    }
}