package com.github.squirrelgrip.extensions.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.*
import java.net.URL
import java.util.HashMap


object Json {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
}

/**
 * Converts Any to a JSON String representation
 */
fun Any.toJson(): String = Json.objectMapper.writeValueAsString(this)
fun Any.toJson(file: File) = Json.objectMapper.writeValue(file, this)
fun Any.toJson(outputStream: OutputStream) = Json.objectMapper.writeValue(outputStream, this)
fun Any.toJson(writer: Writer) = Json.objectMapper.writeValue(writer, this)
fun Any.toJson(dataOutput: DataOutput) = Json.objectMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> JsonParser.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)

fun String.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun InputStream.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun Reader.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun URL.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun ByteArray.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun ByteArray.toJsonNode(offset: Int, length: Int): JsonNode = Json.objectMapper.readTree(this, offset, length)
fun JsonParser.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun File.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)

fun Any.convertToMap(): Map<String, *> =
    Json.objectMapper.convertValue(this, object : TypeReference<HashMap<String, *>>() {}).toMap()
