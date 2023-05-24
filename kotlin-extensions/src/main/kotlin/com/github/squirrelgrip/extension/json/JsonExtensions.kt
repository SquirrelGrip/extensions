package com.github.squirrelgrip.extension.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.github.squirrelgrip.extension.yaml.toYaml
import com.github.squirrelgrip.util.notCatching
import java.io.*
import java.net.URL
import java.nio.file.Path
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * Converts Any to a JSON String representation
 */
fun Any.toJson(objectWriter: (ObjectMapper) -> ObjectWriter): String =
    objectWriter.invoke(Json.objectMapper.copy()).writeValueAsString(this)

fun Any.toJson(): String = Json.objectMapper.writeValueAsString(this)
fun Any.toJson(file: File) = Json.objectMapper.writeValue(file, this)
fun Any.toJson(path: Path) = Json.objectMapper.writeValue(path.toFile(), this)
fun Any.toJson(outputStream: OutputStream) = Json.objectMapper.writeValue(outputStream, this)
fun Any.toJson(writer: Writer) = Json.objectMapper.writeValue(writer, this)
fun Any.toJson(dataOutput: DataOutput) = Json.objectMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(offset: Int, len: Int): T = Json.objectMapper.readValue(this, offset, len, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> JsonParser.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Json.objectMapper.readValue(this, T::class.java)
inline fun <reified T> Path.toInstance(): T = Json.objectMapper.readValue(this.toFile(), T::class.java)

inline fun <reified T> listType() =
    Json.objectMapper.typeFactory.constructCollectionType(List::class.java, T::class.java)

inline fun <reified T> String.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> InputStream.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> Reader.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> URL.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> ByteArray.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> ByteArray.toInstanceList(offset: Int, len: Int): List<T> = Json.objectMapper.readValue(this, offset, len, listType<T>())
inline fun <reified T> DataInput.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> JsonParser.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> File.toInstanceList(): List<T> = Json.objectMapper.readValue(this, listType<T>())
inline fun <reified T> Path.toInstanceList(): List<T> = Json.objectMapper.readValue(this.toFile(), listType<T>())

inline fun <reified T> String.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> InputStream.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> Reader.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> URL.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> ByteArray.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> ByteArray.toJsonStream(offset: Int, length: Int, parallel: Boolean = false): Stream<T> = this.toJsonParser(offset, length).toJsonStream<T>(parallel)
inline fun <reified T> File.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)
inline fun <reified T> Path.toJsonStream(parallel: Boolean = false): Stream<T> = this.toJsonParser().toJsonStream<T>(parallel)

inline fun <reified T> String.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> InputStream.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> Reader.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> URL.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> ByteArray.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> ByteArray.toJsonStream(offset: Int, length: Int): Sequence<T> = this.toJsonParser(offset, length).toJsonSequence<T>()
inline fun <reified T> File.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()
inline fun <reified T> Path.toJsonSequence(): Sequence<T> = this.toJsonParser().toJsonSequence<T>()

inline fun <reified T> JsonParser.toJsonStream(parallel: Boolean = false): Stream<T> =
    StreamSupport.stream(JsonSpliterator(this, T::class.java), parallel)
inline fun <reified T> JsonParser.toJsonSequence(): Sequence<T> =
    JsonSequence(this, T::class.java)

fun String.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun InputStream.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun Reader.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun URL.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun ByteArray.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun ByteArray.toJsonParser(offset: Int, length: Int): JsonParser = Json.objectMapper.createParser(this, offset, length)
fun File.toJsonParser(): JsonParser = Json.objectMapper.createParser(this)
fun Path.toJsonParser(): JsonParser = Json.objectMapper.createParser(this.toFile())

fun String.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun InputStream.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun Reader.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun URL.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun ByteArray.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun ByteArray.toJsonNode(offset: Int, length: Int): JsonNode = Json.objectMapper.readTree(this, offset, length)
fun JsonParser.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun File.toJsonNode(): JsonNode = Json.objectMapper.readTree(this)
fun Path.toJsonNode(): JsonNode = Json.objectMapper.readTree(this.toFile())

fun String.isJson(): Boolean = notCatching { this.toJsonNode() }
fun InputStream.isJson(): Boolean = notCatching { this.toJsonNode() }
fun Reader.isJson(): Boolean = notCatching { this.toJsonNode() }
fun URL.isJson(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isJson(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isJson(offset: Int, length: Int): Boolean = notCatching { this.toJsonNode(offset, length) }
fun JsonParser.isJson(): Boolean = notCatching { this.toJsonNode() }
fun File.isJson(): Boolean = notCatching { this.toJsonNode() }
fun Path.isJson(): Boolean = notCatching { this.toFile().toJsonNode() }

fun Any.convertToMap(): Map<String, *> =
    Json.objectMapper.convertValue(this, object : TypeReference<HashMap<String, *>>() {}).toMap()
