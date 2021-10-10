package com.github.squirrelgrip.extension.xml

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Xml {
    @JsonIgnoreProperties("stackTrace")
    internal class ThrowableMixIn @JsonCreator constructor(
        @JsonProperty("message") message: String?
    ) : Throwable(message)

    var xmlMapper: XmlMapper by XmlMapperDelegate()

    class XmlMapperDelegate : ReadWriteProperty<Xml, XmlMapper> {
        lateinit var value : XmlMapper

        val defaultXmlMapper: XmlMapper by lazy {
            val factoryList = ServiceLoader.load(XmlMapperFactory::class.java).toList()
            if (factoryList.size > 1) {
                throw RuntimeException("Cannot have more than one XmlMapperFactory declared.")
            }
            (factoryList.firstOrNull() ?: (object : XmlMapperFactory {})).getXmlMapper()
        }

        override fun getValue(thisRef: Xml, property: KProperty<*>): XmlMapper {
            return if (!this::value.isInitialized) defaultXmlMapper else value
        }

        override fun setValue(thisRef: Xml, property: KProperty<*>, value: XmlMapper) {
            if (!this::value.isInitialized) {
                this.value = value
            }
        }
    }
}
