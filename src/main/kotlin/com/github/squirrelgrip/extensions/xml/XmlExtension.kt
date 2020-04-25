package com.github.squirrelgrip.extensions.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.*
import java.net.URL

object Xml {
    val xmlMapper = XmlMapper()
}

/**
 * Converts Any to a XML String representation
 */
fun Any.toXml(): String = Xml.xmlMapper.writeValueAsString(this)
fun Any.toXml(file: File) = Xml.xmlMapper.writeValue(file, this)
fun Any.toXml(outputStream: OutputStream) = Xml.xmlMapper.writeValue(outputStream, this)
fun Any.toXml(writer: Writer) = Xml.xmlMapper.writeValue(writer, this)
fun Any.toXml(dataOutput: DataOutput) = Xml.xmlMapper.writeValue(dataOutput, this)

inline fun <reified T> String.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> InputStream.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> Reader.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> URL.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> ByteArray.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> DataInput.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)
inline fun <reified T> File.toInstance(): T = Xml.xmlMapper.readValue(this, T::class.java)