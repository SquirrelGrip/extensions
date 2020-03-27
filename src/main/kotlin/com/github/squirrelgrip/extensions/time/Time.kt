package com.github.squirrelgrip.extensions.time

import java.time.*
import java.util.*

fun Instant.toDate(): Date = Date(this.toEpochMilli())

fun LocalDateTime.toDate(offset: ZoneOffset = ZoneOffset.UTC): Date = this.toInstant(offset).toDate()

fun OffsetDateTime.toDate(): Date = this.toInstant().toDate()

fun ZonedDateTime.toDate(): Date = this.toInstant().toDate()