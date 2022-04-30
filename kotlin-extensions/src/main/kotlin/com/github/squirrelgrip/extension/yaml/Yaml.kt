package com.github.squirrelgrip.extension.yaml

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Yaml {
    @JsonIgnoreProperties("stackTrace")
    internal class ThrowableMixIn @JsonCreator constructor(
        @JsonProperty("message") message: String?
    ) : Throwable(message)

    var yamlMapper: YAMLMapper by YamlMapperDelegate()

    class YamlMapperDelegate : ReadWriteProperty<Yaml, YAMLMapper> {
        lateinit var value: YAMLMapper

        val defaultYamlMapper: YAMLMapper by lazy {
            val factoryList = ServiceLoader.load(YamlMapperFactory::class.java).toList()
            if (factoryList.size > 1) {
                throw RuntimeException("Cannot have more than one YamlMapperFactory declared.")
            }
            (factoryList.firstOrNull() ?: (object : YamlMapperFactory {})).getYAMLMapper()
        }

        override fun getValue(thisRef: Yaml, property: KProperty<*>): YAMLMapper =
            if (!this::value.isInitialized) defaultYamlMapper else value

        override fun setValue(thisRef: Yaml, property: KProperty<*>, value: YAMLMapper) {
            if (!this::value.isInitialized) {
                this.value = value
            }
        }
    }
}
