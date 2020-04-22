package com.github.squirrelgrip.map

import com.github.squirrelgrip.extensions.map.reverse
import com.github.squirrelgrip.extensions.map.reverseWithCollection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapExtensionsTest {
    @Test
    fun `reverseMany() reverses the keys and values to map of value to list of keys`() {
        val map = mapOf(
            1 to "AAA",
            2 to "BBB",
            3 to "BBB"
        )
        assertThat(map.reverse()).isEqualTo(
            mapOf(
                "AAA" to listOf(1),
                "BBB" to listOf(2, 3)
            )
        )
    }

    @Test
    fun `reverseCollection() reverses the keys and values to map of value to list of keys`() {
        val map = mapOf(
            1 to listOf("AAA"),
            2 to listOf("BBB"),
            3 to listOf("AAA", "BBB"),
            4 to listOf("CCC", "BBB")
        )
        assertThat(map.reverseWithCollection()).isEqualTo(
            mapOf(
                "AAA" to listOf(1, 3),
                "BBB" to listOf(2, 3, 4),
                "CCC" to listOf(4)
            )
        )
    }

    @Test
    fun `reverse() reverses the keys and values`() {
        val map = mapOf(
            1 to "AAA",
            2 to "BBB"
        )
        assertThat(map.reverse()).isEqualTo(
            mapOf(
                "AAA" to listOf(1),
                "BBB" to listOf(2)
            )
        )
    }
}