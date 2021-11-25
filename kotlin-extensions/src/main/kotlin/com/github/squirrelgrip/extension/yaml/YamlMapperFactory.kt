package com.github.squirrelgrip.extension.yaml

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

interface YamlMapperFactory {
    fun getYAMLMapper(): YAMLMapper =
        YAMLMapper.builder()
            .addModule(JavaTimeModule())
            .addModule(KotlinModule.Builder().configure(KotlinFeature.StrictNullChecks, true).build())
            .addModule(Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .addMixIn(Throwable::class.java, Yaml.ThrowableMixIn::class.java)
            .build()
}
