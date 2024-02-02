package ru.kima.intelligentchat.domain.tokenizer

import java.util.PriorityQueue

//Thanks to https://github.com/belladoreai/llama-tokenizer-js.git
class LlamaTokenizer(private val vocabulary: List<String>, private val merges: Map<String, Int>) :
    Tokenizer {
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

    override fun encode(
        prompt: String,
        addBosToken: Boolean,
        addPrecedingSpace: Boolean
    ): IntArray {
        if (prompt.isEmpty()) {
            return intArrayOf()
        }

        val tokenIds = mapCharactersToTokenIds(prompt, addBosToken, addPrecedingSpace)
        val mergeQueue = PriorityQueue(prompt.length, comparator)

        fun PriorityQueue<TokenNode>.addNode(node: TokenNode) {
            val mergeIdentifier = node.next?.tokenId?.let { getMergeIdentifier(node.tokenId, it) }

            val priority = merges.getOrDefault(mergeIdentifier, 0)
            if (priority > 0) {
                node.priority = priority.toFloat() + node.pos / prompt.length.toFloat()
                mergeIdentifier?.let {
                    node.mergeToString = it.replace(" ", "")
                }
                add(node)
            }
        }

        var firstTokenNode = TokenNode(
            pos = 0f,
            tokenId = tokenIds.first(),
            prev = null,
            next = null
        )

        var prevTokenNode = firstTokenNode
        for (i in 1..<tokenIds.size) {
            val currTokenNode = TokenNode(
                pos = i.toFloat(),
                tokenId = tokenIds[i],
                prev = prevTokenNode,
                next = null
            )
            prevTokenNode.next = currTokenNode
            mergeQueue.addNode(prevTokenNode)
            prevTokenNode = currTokenNode
        }

        while (mergeQueue.isNotEmpty()) {
            val left = mergeQueue.poll()!!
            //Check if merge is still possible
            if (left.deleted) continue
            if (left.next == null) continue
            if (left.next!!.deleted) continue

            left.deleted = true
            left.next!!.deleted = true

            if (left.prev != null) {
                val oldPrev = left.prev!!
                oldPrev.deleted = true
                val newPrev = TokenNode(
                    pos = oldPrev.pos,
                    tokenId = oldPrev.tokenId,
                    prev = oldPrev.prev,
                    next = oldPrev.next
                )
                left.prev = newPrev

                if (newPrev.prev != null) {
                    newPrev.prev!!.next = newPrev
                } else {
                    firstTokenNode = newPrev
                }
            }

            val resultOfMerge = TokenNode(
                pos = left.pos,
                tokenId = tokens.getOrDefault(left.mergeToString, 0),
                prev = left.prev,
                next = left.next?.next
            )

            if (resultOfMerge.prev != null) {
                resultOfMerge.prev!!.next = resultOfMerge
                mergeQueue.addNode(resultOfMerge.prev!!)
            } else {
                firstTokenNode = resultOfMerge
            }

            if (resultOfMerge.next != null) {
                resultOfMerge.next!!.prev = resultOfMerge
                mergeQueue.addNode(resultOfMerge)
            }
        }

        val mergedTokenIds: MutableList<Int> = ArrayList(prompt.length)
        var currentToken: TokenNode? = firstTokenNode
        while (currentToken != null) {
            mergedTokenIds.add(currentToken.tokenId)
            currentToken = currentToken.next
        }

        return mergedTokenIds.toIntArray()
    }

    private fun mapCharactersToTokenIds(
        prompt: String,
        addBosToken: Boolean,
        addPrecedingSpace: Boolean
    ): IntArray {
        val tokensIds: MutableList<Int> = ArrayList(prompt.length + 1)
        if (addBosToken) {
            tokensIds.add(1)
        }

        // Special: spaces are represented as thick underscore ▁ (id 29871)
        val alteredPrompt = if (addPrecedingSpace) {
            " $prompt"
        } else {
            prompt
        }.replace(" ", thickUnderscore)


        var i = 0
        while (i < alteredPrompt.length) {
            val c = alteredPrompt[i]
            val cStr = c.toString()
            if (tokens.contains(cStr)) {
                val tokenId = tokens[cStr]!!
                tokensIds.add(tokenId)
                i += 1
            } else {
                val utf8String = if (c.isSurrogate() && alteredPrompt.length - i > 1) {
                    alteredPrompt.substring(i, i + 2)
                } else {
                    cStr
                }
                i += utf8String.length

                val bytes = utf8String.toByteArray(Charsets.UTF_8)
                bytes.forEach {
                    val hexToken = byteToHex(it)
                    val tokenId = tokens.getOrDefault(hexToken, 0)
                    tokensIds.add(tokenId)
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

    private fun getMergeIdentifier(id1: Int, id2: Int): String {
        return "${vocabulary[id1]} ${vocabulary[id2]}"
    }

    private val comparator: Comparator<TokenNode> = compareBy { it.priority }

    private data class TokenNode(
        val pos: Float,
        val tokenId: Int,
        var prev: TokenNode?,
        var next: TokenNode?,
        var priority: Float = 0f,
        var mergeToString: String = String(),
        var deleted: Boolean = false
    )
}