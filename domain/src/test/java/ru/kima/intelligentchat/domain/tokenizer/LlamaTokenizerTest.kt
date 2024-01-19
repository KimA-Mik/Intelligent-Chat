package ru.kima.intelligentchat.domain.tokenizer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class LlamaTokenizerTest {

    val tokenizer: LlamaTokenizer

    init {
        val vocabulary = File("src/main/res/raw/llama_vocabulary").bufferedReader().readLines()
        val merges = mutableMapOf<String, Int>()
        File("src/main/res/raw/llama_merges").bufferedReader()
            .readText()
            .split('\n')
            .dropLast(1)
            .forEach { line ->
                val parts = line.split(',')
                merges[parts.first()] = parts.last().toInt()
            }

        tokenizer = LlamaTokenizer(vocabulary, merges)
    }

//    @BeforeEach
//    fun setUp() {
//    }

    @Test
    fun encode() {
//        assertEquals(
//            intArrayOf(1, 15043, 3186, 29991),
//            tokenizer.encode("Hello world!")
//        )

        assertEquals(
            intArrayOf(1, 2646, 1327, 287),
            tokenizer.encode("grabbed")
        )

        assertEquals(
            intArrayOf(1, 29871, 2646, 1327, 287),
            tokenizer.encode(" grabbed")
        )

        // Naive implementation uses incorrect merge order for multiple consecutive space merges, making this a good test case
        assertEquals(
            intArrayOf(1, 9651, 2646, 1327, 287),
            tokenizer.encode("           grabbed")
        )

        // Linebreaks and tabs are handled as fallback to byte tokens
        assertEquals(
            intArrayOf(1, 29871, 13),
            tokenizer.encode("\n")
        )
        assertEquals(
            intArrayOf(1, 259, 13),
            tokenizer.encode(" \n")
        )
        assertEquals(
            intArrayOf(1, 29871, 12, 21175, 12, 12, 12, 12, 449, 1244),
            tokenizer.encode("	tabs				out here")
        )

        // Equal prio merges are performed left-to-right (fixed in 1.1.1)
        assertEquals(
            intArrayOf(1, 4853, 13, 4136, 13, 833, 29877),
            tokenizer.encode("ax\n####\nboo")
        )

        // UTF-8 multipoint character that should be found in vocabulary
        assertEquals(
            intArrayOf(1, 29871, 30411),
            tokenizer.encode("镇")
        )

        // UTF-8 multipoint character that should NOT be found in vocabulary, fallback to MULTIPLE byte tokens
        assertEquals(
            intArrayOf(1, 29871, 243, 162, 169, 156),
            tokenizer.encode("🦙")
        )

        // Consecutive UTF-8 multipoint characters that are NOT found in a vocabulary and use DIFFERENT number of bytes
        assertEquals(
            intArrayOf(1, 29871, 243, 162, 169, 156, 237, 156, 141),
            tokenizer.encode("🦙Ꙋ")
        )
        assertEquals(
            intArrayOf(1, 29871, 237, 156, 141, 243, 162, 169, 156),
            tokenizer.encode("Ꙋ🦙")
        )

    }
}