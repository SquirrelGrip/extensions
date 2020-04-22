package com.github.squirrelgrip.map

import com.github.squirrelgrip.extensions.map.reverse
import com.github.squirrelgrip.extensions.map.reverseMany
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
        assertThat(map.reverseMany()).isEqualTo(
            mapOf(
                "AAA" to listOf(1),
                "BBB" to listOf(2, 3)
            )
        )
        assertThat(map.reverse()).isEqualTo(
            mapOf(
                "AAA" to 1,
                "BBB" to 3
            )
        )
    }

    @Test
    fun `reverse() reverses the keys and values`() {
        val map = mapOf(
            1 to "AAA",
            2 to "BBB"
        )
        assertThat(map.reverseMany()).isEqualTo(
            mapOf(
                "AAA" to listOf(1),
                "BBB" to listOf(2)
            )
        )
        assertThat(map.reverse()).isEqualTo(
            mapOf(
                "AAA" to 1,
                "BBB" to 2
            )
        )
    }
}