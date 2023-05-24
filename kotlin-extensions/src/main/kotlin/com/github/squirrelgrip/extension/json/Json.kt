package com.github.squirrelgrip.extension.json

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.squirrelgrip.util.applyIfTrue
import java.util.*
import java.util.function.Consumer
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Json {
    @JsonIgnoreProperties("stackTrace")
    internal class ThrowableMixIn @JsonCreator constructor(
        @JsonProperty("message") message: String?
    ) : Throwable(message)

    val objectMapper: ObjectMapper by ObjectMapperDelegate()

    class ObjectMapperDelegate : ReadOnlyProperty<Json, ObjectMapper> {
        private lateinit var value: ObjectMapper

        private val defaultObjectMapper: ObjectMapper by lazy {
            val factoryList = ServiceLoader.load(ObjectMapperFactory::class.java).toList()
            if (factoryList.size > 1) {
                throw RuntimeException("Cannot have more than one ObjectMapperFactory declared.")
            }
            (factoryList.firstOrNull() ?: (object : ObjectMapperFactory {})).getObjectMapper().findAndRegisterModules()
                .also {
                    it.registerModule(JavaTimeModule())
                }
        }

        override fun getValue(thisRef: Json, property: KProperty<*>): ObjectMapper =
            if (!this::value.isInitialized) defaultObjectMapper else value
    }
}

class JsonSpliterator<T>(
    private val parser: JsonParser,
    private val type: Class<T>
): Spliterator<T> {
    private val iterator: Iterator<T> = JsonIterator(parser, type)

    override fun tryAdvance(action: Consumer<in T>): Boolean =
        iterator.hasNext().applyIfTrue {
                action.accept(iterator.next())
        }

    override fun trySplit(): Spliterator<T>? = null

    override fun estimateSize(): Long  = Long.MAX_VALUE

    override fun characteristics(): Int = 0
}

class JsonSequence<T>(
    val jsonParser: JsonParser,
    val type: Class<T>
) : Sequence<T> {
    override fun iterator(): Iterator<T> =
        JsonIterator(jsonParser, type)
}

class JsonIterator<T>(
    val jsonParser: JsonParser,
    val type: Class<T>
) : Iterator<T> {
    init {
        if(jsonParser.nextToken() != JsonToken.START_ARRAY) {
            throw Exception("json must be an array")
        }
    }

    private var currentValue: T? = next()

    override fun hasNext(): Boolean = currentValue != null

    override fun next(): T {
        return currentValue!!.apply {
            currentValue = if(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.readValueAs(type)
            } else {
                jsonParser.close()
                null
            }
        }
    }
}
