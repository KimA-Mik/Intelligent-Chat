package ru.kima.intelligentchat.domain.utils

import android.util.Base64
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class PngBlockParser {
    private val pngSignature = byteArrayOf(137.toByte(), 80, 78, 71, 13, 10, 26, 10)

    private enum class BLOCK(val type: Int) {
        //additional png blocks
        @Suppress("unused")
        IHDR(1229472850),
        tEXt(1950701684),

        @Suppress("unused")
        IDAT(1229209940),
        IEND(1229278788)
    }

    fun getTextBlock(bytes: ByteArray): String {
        val fileSignature = bytes.sliceArray(0..7)
        val isPng = pngSignature.contentEquals(fileSignature)

        if (!isPng) {
            throw Exception("This is not a png file")
        }

        var resultBuffer: ByteBuffer? = null


        val buffer = ByteBuffer.wrap(bytes)
        buffer.position(8)

        while (buffer.hasRemaining()) {
            //no UInt
            val length = buffer.int
            val type = buffer.int

            when (type) {
                BLOCK.tEXt.type -> {
                    resultBuffer = buffer.slice()
                    resultBuffer.limit(length)
                    break
                }

                BLOCK.IEND.type -> break
            }

            buffer.position(buffer.position() + length)
            @Suppress("UNUSED_VARIABLE") val crc = buffer.int
        }

        if (resultBuffer == null)
            return String()

        return parseTextChunk(resultBuffer)
    }

    private fun parseTextChunk(buffer: ByteBuffer): String {
        val separator: Byte = 0
        var separatorPosition = 0

        for (i in 0..<buffer.limit()) {
            if (buffer.get(i) == separator) {
                separatorPosition = i
                break
            }
        }

        val keywordBuffer = buffer.slice()
        keywordBuffer.limit(separatorPosition)
        @Suppress("UNUSED_VARIABLE") val keyword =
            StandardCharsets.US_ASCII.decode(keywordBuffer).toString()

        buffer.position(separatorPosition + 1)
        val toDecode = buffer.slice()
        val decodedArray = Base64.decode(
            toDecode.array(),
            toDecode.arrayOffset(),
            toDecode.limit(),
            Base64.DEFAULT
        )
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(decodedArray)).toString()
    }
}