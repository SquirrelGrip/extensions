package com.github.squirrelgrip.extension.json

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.*
import java.net.URL
import java.util.*


interface ObjectMapperFactory {
    fun getObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .registerModule(Jdk8Module()).apply {
                addMixIn(Throwable::class.java, Json.ThrowableMixIn::class.java)
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            }
    }
}

object Json {
    @JsonIgnoreProperties("stackTrace")
    internal class ThrowableMixIn @JsonCreator constructor(@JsonProperty("message") message: String?) :
        Throwable(message)

    val objectMapper: ObjectMapper by lazy {
        val factoryList = ServiceLoader.load(ObjectMapperFactory::class.java).toList()
        if (factoryList.size > 1) {
            throw RuntimeException("Cannot have more than one ObjectMapperFactory declared.")
        }
        (factoryList.firstOrNull() ?: (object : ObjectMapperFactory {})).getObjectMapper()
    }
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

inline fun <reified T> listType() =
    Json.objectMapper.typeFactory.constructCollectionType(List::class.java, T::class.java)

inline fun <reified T> String.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> InputStream.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> Reader.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> URL.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> ByteArray.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> DataInput.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> JsonParser.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> File.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())

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
