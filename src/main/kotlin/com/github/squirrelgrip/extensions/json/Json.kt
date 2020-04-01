package com.github.squirrelgrip.extensions.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.*
import java.net.URL

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

fun <T> String.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> InputStream.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> Reader.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> URL.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> File.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> ByteArray.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> DataInput.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)
fun <T> JsonParser.toInstance(clazz: Class<T>): T = Json.objectMapper.readValue(this, clazz)