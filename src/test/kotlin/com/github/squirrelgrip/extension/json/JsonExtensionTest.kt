package com.github.squirrelgrip.extension.json

import com.github.squirrelgrip.Sample
import com.github.squirrelgrip.extension.map.flatten
import jdk.internal.org.objectweb.asm.TypeReference
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class JsonExtensionTest {
    @Test
    fun `Any convertToMap`() {
        assertThat(Sample().convertToMap().flatten()).isEqualTo(mapOf("m/a" to "AAA", "s" to "A Simple String", "v" to 0, "l/0" to "1","l/1" to "AAA"))
        assertThat(Sample().convertToMap()).isEqualTo(mapOf("m" to mapOf("a" to "AAA"), "s" to "A Simple String", "v" to 0, "l" to listOf("1", "AAA")))
    }

    @Test
    fun `toJson`() {
        assertThat(Sample().toJson()).isEqualTo("""{"v":0,"s":"A Simple String","m":{"a":"AAA"},"l":["1","AAA"]}""")
        assertThat("""{"v":0,"s":"A Simple String","m":{"a":"AAA"},"l":["1","AAA"]}""".toInstance<Sample>()).isEqualTo(Sample())
    }

    @Test
    fun `toInstanceList`() {
        assertThat(listOf(Sample(), Sample()).toJson()).isEqualTo("[{\"v\":0,\"s\":\"A Simple String\",\"m\":{\"a\":\"AAA\"},\"l\":[\"1\",\"AAA\"]},{\"v\":0,\"s\":\"A Simple String\",\"m\":{\"a\":\"AAA\"},\"l\":[\"1\",\"AAA\"]}]")
        assertThat("[{\"v\":0,\"s\":\"A Simple String\",\"m\":{\"a\":\"AAA\"},\"l\":[\"1\",\"AAA\"]},{\"v\":0,\"s\":\"A Simple String\",\"m\":{\"a\":\"AAA\"},\"l\":[\"1\",\"AAA\"]}]".toInstanceList<Sample>()).isEqualTo(listOf(Sample(), Sample()))
    }

    @Test
    fun `exception to json`() {
        assertThat(Exception("Message").toJson()).isEqualTo("{\"cause\":null,\"message\":\"Message\",\"localizedMessage\":\"Message\",\"suppressed\":[]}")
        val exception =
            "{\"cause\":null,\"message\":\"Message\",\"localizedMessage\":\"Message\",\"suppressed\":[]}".toInstance<Exception>()
        assertThat(exception.message).isEqualTo(Exception("Message").message)
        assertThat(exception.cause).isEqualTo(Exception("Message").cause)
        assertThat(exception.localizedMessage).isEqualTo(Exception("Message").localizedMessage)
    }
}
