package com.github.squirrelgrip.extension.time

import java.lang.UnsupportedOperationException
import java.time.*
import java.time.ZoneOffset.UTC
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.temporal.Temporal
import java.util.*

/**
 * Converts an Instant to java.util.Date
 */
fun Instant.toDate(): Date = Date(this.toEpochMilli())

/**
 * Converts an LocalDateTime to java.util.Date
 */
fun LocalDateTime.toDate(offset: ZoneOffset = UTC): Date = this.toInstant(offset).toDate()

/**
 * Converts an OffsetDateTime to java.util.Date
 */
fun OffsetDateTime.toDate(): Date = this.toInstant().toDate()

/**
 * Converts an ZonedDateTime to java.util.Date
 */
fun ZonedDateTime.toDate(): Date = this.toInstant().toDate()

/**
 * Converts an Instant to OffsetDateTime
 */
fun Instant.toOffsetDateTime(offset: ZoneOffset = UTC): OffsetDateTime = this.atOffset(offset)

/**
 * Converts an Instant to ZonedDateTime
 */
fun Instant.toZonedDateTime(zone: ZoneId = UTC): ZonedDateTime = this.atZone(zone)

/**
 * Converts an Instant to LocalDateTime
 */
fun Instant.toLocalDateTime(zone: ZoneId = UTC): LocalDateTime = LocalDateTime.ofInstant(this, zone)

/**
 * Converts an Instant to LocalDate
 */
fun Instant.toLocalDate(zone: ZoneId = UTC): LocalDate = LocalDate.ofInstant(this, zone)

fun Temporal.toInstant(zone: ZoneOffset = UTC): Instant {
    return when (this) {
        is ChronoLocalDateTime<*> -> toInstant(zone)
        is ChronoZonedDateTime<*> -> toInstant()
        is OffsetDateTime -> toInstant()
        is Instant -> this
        else -> throw UnsupportedOperationException()
    }
}

fun Instant.isEqualOrBefore(instant: Instant) = !this.isAfter(instant)
fun Instant.isEqualOrAfter(instant: Instant) = !this.isBefore(instant)
fun LocalDate.isEqualOrBefore(date: LocalDate) = !this.isAfter(date)
fun LocalDate.isEqualOrAfter(date: LocalDate) = !this.isBefore(date)

fun DateInterval.toInterval(zone: ZoneId = UTC): Interval =
    Interval.of(this.start.atStartOfDay(zone), this.end.atStartOfDay(zone))