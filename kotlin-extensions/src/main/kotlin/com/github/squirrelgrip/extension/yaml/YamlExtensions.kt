package com.github.squirrelgrip.extension.yaml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.github.squirrelgrip.extension.json.toJsonNode
import com.github.squirrelgrip.extension.xml.Xml
import com.github.squirrelgrip.util.notCatching
import java.io.*
import java.net.URL
import java.nio.file.Path

/**
 * Converts Any to a XML String representation
 */
fun Any.toYaml(): String = Yaml.yamlMapper.writeValueAsString(this)
fun Any.toYaml(file: File) = Yaml.yamlMapper.writeValue(file, this)
fun Any.toYaml(path: Path) = Yaml.yamlMapper.writeValue(path.toFile(), this)
fun Any.toYaml(outputStream: OutputStream) = Yaml.yamlMapper.writeValue(outputStream, this)
fun Any.toYaml(writer: Writer) = Yaml.yamlMapper.writeValue(writer, this)
fun Any.toYaml(dataOutput: DataOutput) = Yaml.yamlMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(offset: Int, len: Int): T = Yaml.yamlMapper.readValue(this, offset, len, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> Path.toInstance(): T = Yaml.yamlMapper.readValue(this.toFile(), T::class.java)

fun String.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun InputStream.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun Reader.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun URL.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun ByteArray.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun ByteArray.toJsonNode(offset: Int, length: Int): JsonNode = Yaml.yamlMapper.readTree(this, offset, length)
fun JsonParser.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun File.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this)
fun Path.toJsonNode(): JsonNode = Yaml.yamlMapper.readTree(this.toFile())

fun String.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun InputStream.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun Reader.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun URL.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun ByteArray.isYaml(offset: Int, length: Int): Boolean = notCatching { this.toJsonNode(offset, length) }
fun JsonParser.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun File.isYaml(): Boolean = notCatching { this.toJsonNode() }
fun Path.isYaml(): Boolean = notCatching { this.toFile().toJsonNode() }
