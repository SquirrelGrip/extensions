package com.github.squirrelgrip.extensions.time

import java.time.*
import java.util.*

/**
 * Converts an Instant to java.util.Date
 */
fun Instant.toDate(): Date = Date(this.toEpochMilli())

/**
 * Converts an LocalDateTime to java.util.Date
 */
fun LocalDateTime.toDate(offset: ZoneOffset = ZoneOffset.UTC): Date = this.toInstant(offset).toDate()

/**
 * Converts an OffsetDateTime to java.util.Date
 */
fun OffsetDateTime.toDate(): Date = this.toInstant().toDate()

/**
 * Converts an ZonedDateTime to java.util.Date
 */
fun ZonedDateTime.toDate(): Date = this.toInstant().toDate()