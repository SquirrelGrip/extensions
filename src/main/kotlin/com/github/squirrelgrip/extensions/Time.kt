package com.github.squirrelgrip.extensions

import java.time.*
import java.util.*

inline fun Instant.toDate(): Date = Date(this.toEpochMilli())

inline fun LocalDateTime.toDate(offset: ZoneOffset = ZoneOffset.UTC): Date = this.toInstant(offset).toDate()

inline fun OffsetDateTime.toDate(): Date = this.toInstant().toDate()

inline fun ZonedDateTime.toDate(): Date = this.toInstant().toDate()