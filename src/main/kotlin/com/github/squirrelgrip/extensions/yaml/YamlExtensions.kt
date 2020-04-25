package com.github.squirrelgrip.extensions.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.*
import java.net.URL


object Yaml {
    val yamlMapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
}

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
