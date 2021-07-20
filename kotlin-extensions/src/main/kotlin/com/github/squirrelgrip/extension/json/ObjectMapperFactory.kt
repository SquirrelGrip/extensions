package com.github.squirrelgrip.extension.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

interface ObjectMapperFactory {
    fun getObjectMapper(): ObjectMapper =
        JsonMapper.builder()
            .addModule(JavaTimeModule())
            .addModule(KotlinModule())
            .addModule(Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .addMixIn(Throwable::class.java, Json.ThrowableMixIn::class.java)
            .build()
}
