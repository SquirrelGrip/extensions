package com.github.squirrelgrip.extension.yaml

import java.io.*
import java.net.URL

/**
 * Converts Any to a XML String representation
 */
fun Any.toYaml(): String = Yaml.yamlMapper.writeValueAsString(this)
fun Any.toYaml(file: File) = Yaml.yamlMapper.writeValue(file, this)
fun Any.toYaml(outputStream: OutputStream) = Yaml.yamlMapper.writeValue(outputStream, this)
fun Any.toYaml(writer: Writer) = Yaml.yamlMapper.writeValue(writer, this)
fun Any.toYaml(dataOutput: DataOutput) = Yaml.yamlMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Yaml.yamlMapper.readValue(this, T::class.java)
