package com.github.squirrelgrip.extensions

import java.time.*
import java.util.*

fun Instant.toDate() = Date(this.toEpochMilli())

fun LocalDateTime.toDate(offset: ZoneOffset = ZoneOffset.UTC) = this.toInstant(offset)

fun OffsetDateTime.toDate() = this.toInstant().toDate()

fun ZonedDateTime.toDate() = this.toInstant().toDate()