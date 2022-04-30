package com.github.squirrelgrip.extension.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.Temporal
import java.util.stream.Stream

internal class TimeExtensionsKtTest {
    companion object {
        val now = Instant.now()
        val utc = ZoneOffset.UTC
        val singapore = ZoneOffset.ofHours(8)
        val alaska = ZoneOffset.ofHours(-8)

        @JvmStatic
        fun toInstance(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(now, utc),
                Arguments.of(now.toOffsetDateTime(), utc),
                Arguments.of(now.toOffsetDateTime(singapore), utc),
                Arguments.of(now.toOffsetDateTime(), singapore),
                Arguments.of(now.toOffsetDateTime(), alaska),

                Arguments.of(now.toZonedDateTime(), utc),
                Arguments.of(now.toZonedDateTime(singapore), utc),
                Arguments.of(now.toZonedDateTime(), singapore),
                Arguments.of(now.toZonedDateTime(), alaska),

                Arguments.of(now.toLocalDateTime(), utc),
                Arguments.of(now.toLocalDateTime(utc), utc),
                Arguments.of(now.toLocalDateTime(singapore), singapore),
                Arguments.of(now.toLocalDateTime(alaska), alaska),
            )
        }
    }

    @ParameterizedTest
    @MethodSource
    fun toInstance(testSubject: Temporal, zone: ZoneOffset) {
        assertThat(testSubject.toInstant(zone)).isEqualTo(now)
    }
}
