package com.github.squirrelgrip.extension.json

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Json {
    @JsonIgnoreProperties("stackTrace")
    internal class ThrowableMixIn @JsonCreator constructor(
        @JsonProperty("message") message: String?
    ) : Throwable(message)

    var objectMapper: ObjectMapper by ObjectMapperDelegate()

    class ObjectMapperDelegate : ReadWriteProperty<Json, ObjectMapper> {
        lateinit var value : ObjectMapper

        val defaultObjectMapper: ObjectMapper by lazy {
            val factoryList = ServiceLoader.load(ObjectMapperFactory::class.java).toList()
            if (factoryList.size > 1) {
                throw RuntimeException("Cannot have more than one ObjectMapperFactory declared.")
            }
            (factoryList.firstOrNull() ?: (object : ObjectMapperFactory {})).getObjectMapper().findAndRegisterModules().also {
                it.registerModule(JavaTimeModule())
            }
        }

        override fun getValue(thisRef: Json, property: KProperty<*>): ObjectMapper {
            return if (!this::value.isInitialized) defaultObjectMapper else value
        }

        override fun setValue(thisRef: Json, property: KProperty<*>, value: ObjectMapper) {
            if (!this::value.isInitialized) {
                this.value = value
            }
        }
    }
}
