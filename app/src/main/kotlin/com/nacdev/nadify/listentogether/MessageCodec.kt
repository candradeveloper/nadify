/**
 * Nadify Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.nacdev.nadify.listentogether

import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Codec for encoding and decoding messages.
 * Note: Protobuf implementation is currently disabled due to missing schema.
 */
class MessageCodec(
    var compressionEnabled: Boolean = false
) {
    companion object {
        private const val TAG = "MessageCodec"
        private const val COMPRESSION_THRESHOLD = 100 // Only compress if > 100 bytes
    }
    
    /**
     * Encode a message (Stubbed)
     */
    fun encode(msgType: String, payload: Any?): ByteArray {
        Timber.tag(TAG).w("Encoding disabled: missing protobuf schema")
        return byteArrayOf()
    }
    
    /**
     * Decode a message (Stubbed)
     */
    fun decode(data: ByteArray): Pair<String, ByteArray> {
        Timber.tag(TAG).w("Decoding disabled: missing protobuf schema")
        return Pair("", byteArrayOf())
    }
    
    /**
     * Decode payload (Stubbed)
     */
    fun decodePayload(msgType: String, payloadBytes: ByteArray): Any? {
        Timber.tag(TAG).w("Payload decoding disabled: missing protobuf schema")
        return null
    }

    /**
     * Compress data using GZIP
     */
    private fun compressData(data: ByteArray): ByteArray {
        val outputStream = ByteArrayOutputStream()
        GZIPOutputStream(outputStream).use { gzip ->
            gzip.write(data)
        }
        return outputStream.toByteArray()
    }
    
    /**
     * Decompress GZIP data
     */
    private fun decompressData(data: ByteArray): ByteArray? {
        return try {
            val inputStream = ByteArrayInputStream(data)
            GZIPInputStream(inputStream).use { gzip ->
                gzip.readBytes()
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to decompress data")
            null
        }
    }
}
