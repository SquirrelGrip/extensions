package com.github.squirrelgrip.extensions

import java.time.*
import java.util.*

inline fun Instant.toDate() = Date(this.toEpochMilli())

inline fun LocalDateTime.toDate(offset: ZoneOffset = ZoneOffset.UTC) = this.toInstant(offset)

inline fun OffsetDateTime.toDate() = this.toInstant().toDate()

inline fun ZonedDateTime.toDate() = this.toInstant().toDate()