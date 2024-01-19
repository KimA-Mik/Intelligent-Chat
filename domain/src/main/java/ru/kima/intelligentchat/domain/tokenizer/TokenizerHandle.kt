package ru.kima.intelligentchat.domain.tokenizer

import android.content.Context
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kima.intelligentchat.domain.R

class TokenizerHandle(private val context: Context) {
    private var _type = Tokenizer.Type.LLama
    private val tokenizer: MutableStateFlow<Tokenizer>

    init {
        val t = createLLamaTokenizer()
        tokenizer = MutableStateFlow(t)
    }

    suspend fun collect(collector: FlowCollector<Tokenizer>): Nothing = tokenizer.collect(collector)

    internal fun selectTokenizer(type: Tokenizer.Type) {
        if (_type == type) {
            return
        }

        _type = type
        tokenizer.value = when (type) {
            Tokenizer.Type.LLama -> createLLamaTokenizer()
        }
    }

    private fun createLLamaTokenizer(): Tokenizer {
        val rawVocabulary = context.resources.openRawResource(R.raw.llama_vocabulary)
        val vocabulary = rawVocabulary.bufferedReader().readLines()
        rawVocabulary.close()

        val rawMerges = context.resources.openRawResource(R.raw.llama_merges)
        val merges = mutableMapOf<String, Int>()
        rawMerges.bufferedReader().useLines { lines ->
            lines.forEach {
                val parts = it.split(',')
                merges[parts.first()] = parts.last().toInt()
            }
        }
        rawMerges.close()

        return LlamaTokenizer(vocabulary, merges)
    }
}