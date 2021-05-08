package com.github.squirrelgrip.util

import com.github.squirrelgrip.extension.time.isEqualOrAfter
import com.github.squirrelgrip.extension.time.isEqualOrBefore
import java.time.Duration
import java.time.Instant
import java.time.Period

class Interval private constructor(
    val start: Instant,
    val end: Instant
) {
    companion object {
        fun of(start: Instant, end: Instant): Interval =
            if (start.isEqualOrBefore(end)) Interval(
                start,
                end
            ) else throw IllegalArgumentException("Start is after end")

        fun of(start: Instant, period: Period): Interval =
            if (period.isNegative) of(start.plus(period), start) else of(start, start.plus(period))

        fun of(period: Period, end: Instant): Interval =
            if (period.isNegative) of(end, end.minus(period)) else of(end.minus(period), end)

        fun of(start: Instant, duration: Duration): Interval =
            if (duration.isNegative) of(start.plus(duration), start) else of(start, start.plus(duration))

        fun of(duration: Duration, end: Instant): Interval =
            if (duration.isNegative) of(end, end.minus(duration)) else of(end.minus(duration), end)

        fun parse(str: String): Interval {
            val split = str.split("/")
            return when {
                split[0].matches(Regex("[-+]?P.*")) -> of(Duration.parse(split[0]), Instant.parse(split[1]))
                split[1].matches(Regex("[-+]?P.*")) -> of(Instant.parse(split[0]), Duration.parse(split[1]))
                else -> of(Instant.parse(split[0]), Instant.parse(split[1]))
            }
        }

    }

    fun toDuration(): Duration = Duration.between(start, end)

    fun isBefore(instant: Instant): Boolean = end.isBefore(instant)

    fun isBefore(interval: Interval): Boolean = isBefore(interval.end)

    fun isAfter(instant: Instant): Boolean = start.isAfter(instant)

    fun isAfter(interval: Interval): Boolean = isAfter(interval.start)

    fun contains(instant: Instant): Boolean = (instant.isEqualOrAfter(start)) && instant.isBefore(end)

    //    Does this interval abut with the interval specified.
    fun abuts(interval: Interval): Boolean = interval.end == start || interval.start == end

    fun overlaps(interval: Interval): Boolean = contains(interval.start) || contains(interval.end)

    fun contains(interval: Interval): Boolean = contains(interval.start) && interval.end.isEqualOrBefore(end)

    //    Gets the gap between this interval and another interval.
    fun gap(interval: Interval): Interval? =
        if (abuts(interval) || overlaps(interval)) {
            null
        } else if (isBefore(interval)) {
            of(end, interval.start)
        } else {
            of(interval.end, start)
        }

    //    Gets the overlap between this interval and another interval.
    fun overlap(interval: Interval): Interval? =
        if (overlaps(interval)) {
            val start = if (this.start.isBefore(interval.start)) interval.start else this.start
            val end = if (interval.end.isAfter(this.end)) this.end else interval.end
            of(start, end)
        } else {
            null
        }

    fun join(interval: Interval): Interval? =
        when {
            abuts(interval) || overlaps(interval) -> {
                val start = if (this.start.isBefore(interval.start)) this.start else interval.start
                val end = if (interval.end.isAfter(this.end)) interval.end else this.end
                of(start, end)
            }
            else -> throw IllegalArgumentException("Interval does not abut or overlap.")
        }

    //    Creates a new interval with the specified duration after the start instant.
    fun withDurationAfterStart(duration: Duration): Interval = of(start, duration)

    //    Creates a new interval with the specified duration before the end instant.
    fun withDurationBeforeEnd(duration: Duration): Interval = of(duration, end)

    //    Creates a new interval with the specified period after the start instant.
    fun withPeriodAfterStart(period: Period): Interval = of(start, period)

    //    Creates a new interval with the specified period before the end instant.
    fun withPeriodBeforeEnd(period: Period): Interval = of(period, end)

    //    Creates a new interval with the specified start instant.
    fun withStart(start: Instant): Interval = of(start, end)

    //    Creates a new interval with the specified end instant.
    fun withEnd(end: Instant): Interval = of(start, end)

    override fun toString(): String = "$start/$end"

    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Interval
                start == other.start && end == other.end
            }
        }

    override fun hashCode(): Int = 31 * start.hashCode() + end.hashCode()

}
