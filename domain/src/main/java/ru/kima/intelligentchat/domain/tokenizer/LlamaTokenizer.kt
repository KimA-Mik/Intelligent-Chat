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
        val tokenIds = mapCharactersToTokenIds(prompt, addBosToken, addPrecedingSpace)
        val mergeQueue = PriorityQueue(comparator)

        fun PriorityQueue<TokenNode>.addNode(node: TokenNode) {
            val mergeIdentifier = node.next?.tokenId?.let { getMergeIdentifier(node.tokenId, it) }

            val mergePriority = merges.getOrDefault(mergeIdentifier, 0)
                .toFloat() + node.pos / prompt.length.toFloat()
            node.priority = mergePriority
            mergeIdentifier?.let {
                node.mergeToString = it.replace(" ", "")
            }
            add(node)
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

    private fun getMergeIdentifier(id1: Int, id2: Int): String {
        return "${vocabulary[id1]} ${vocabulary[id2]}"
    }


    private val comparator: Comparator<TokenNode> = compareByDescending { it.priority }

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