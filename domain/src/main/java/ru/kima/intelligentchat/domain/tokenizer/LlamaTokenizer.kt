package ru.kima.intelligentchat.domain.tokenizer

//Thanks to https://github.com/belladoreai/llama-tokenizer-js.git
class LlamaTokenizer(vocabulary: List<String>, merges: Map<String, Int>) {
    private val tokens: Map<String, Int>
    private val thickUnderscore: String

    init {
        val t = mutableMapOf<String, Int>()
        vocabulary.forEachIndexed { index, token ->
            t[token] = index
        }
        tokens = t
        thickUnderscore = vocabulary[29871]
    }

    fun encode(prompt: String, addBosToken: Boolean = true, addPrecedingSpace: Boolean = true) {
        val tokenIds = mapCharactersToTokenIds(prompt, addBosToken, addPrecedingSpace)
    }

    private fun mapCharactersToTokenIds(
        p: String,
        addBosToken: Boolean,
        addPrecedingSpace: Boolean
    ): IntArray {
        val tokensIds: MutableList<Int> = ArrayList(p.length)
        if (addBosToken) {
            tokensIds.add(1)
        }

        val prompt = if (!addPrecedingSpace) {
            p
        } else {
            " $p"
        }

        // Special: spaces are represented as thick underscore ▁ (id 29871)
        val alteredPrompt = prompt.replace(" ", thickUnderscore)

        for (c in alteredPrompt) {
            val cStr = c.toString()
            if (tokens.containsKey(cStr)) {
                tokensIds.add(tokens[cStr]!!)
            } else {
                for (i in 0..3) {
                    val byte: Byte = (c.code shr i * 8).toByte()
                    val hexToken = byteToHex(byte)
                    val tokenId = tokens[hexToken]
                    if (tokenId == null) {
                        //unknown token
                        tokensIds.add(0)
                    } else {
                        tokensIds.add(tokenId)
                    }
                }
            }
        }

        return tokensIds.toIntArray()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun byteToHex(byte: Byte): String {
        val hexValue = byte
            .toHexString(HexFormat.UpperCase)
        return "<0x$hexValue>"
    }
}