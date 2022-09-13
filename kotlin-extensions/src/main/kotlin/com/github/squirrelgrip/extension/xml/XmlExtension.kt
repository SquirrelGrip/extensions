package com.github.squirrelgrip.extension.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.github.squirrelgrip.extension.json.Json
import com.github.squirrelgrip.extension.json.toJsonNode
import com.github.squirrelgrip.util.notCatching
import java.io.*
import java.net.URL
import java.nio.file.Path

/**
 * Converts Any to a XML String representation
 */
fun Any.toXml(): String = Xml.xmlMapper.writeValueAsString(this)
fun Any.toXml(file: File) = Xml.xmlMapper.writeValue(file, this)
fun Any.toXml(path: Path) = Xml.xmlMapper.writeValue(path.toFile(), this)
fun Any.toXml(outputStream: OutputStream) = Xml.xmlMapper.writeValue(outputStream, this)
fun Any.toXml(writer: Writer) = Xml.xmlMapper.writeValue(writer, this)
fun Any.toXml(dataOutput: DataOutput) = Xml.xmlMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(offset: Int, len: Int): T = Json.objectMapper.readValue(this, offset, len, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> Path.toInstance(): T = Xml.xmlMapper.readValue(this.toFile(), T::class.java)

fun String.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun InputStream.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun Reader.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun URL.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun ByteArray.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun ByteArray.toJsonNode(offset: Int, length: Int): JsonNode = Xml.xmlMapper.readTree(this, offset, length)
fun JsonParser.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun File.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this)
fun Path.toJsonNode(): JsonNode = Xml.xmlMapper.readTree(this.toFile())

fun String.isXml(): Boolean = notCatching { this.toJsonNode() }
fun InputStream.isXml(): Boolean = notCatching { this.toJsonNode() }
fun Reader.isXml(): Boolean = notCatching { this.toJsonNode() }
fun URL.isXml(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isXml(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isXml(offset: Int, length: Int): Boolean = notCatching { this.toJsonNode(offset, length) }
fun JsonParser.isXml(): Boolean = notCatching { this.toJsonNode() }
fun File.isXml(): Boolean = notCatching { this.toJsonNode() }
fun Path.isXml(): Boolean = notCatching { this.toFile().toJsonNode() }
