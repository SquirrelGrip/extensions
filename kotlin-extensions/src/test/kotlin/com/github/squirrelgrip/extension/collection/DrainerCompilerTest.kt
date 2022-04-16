package com.github.squirrelgrip.extension.collection

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DrainerCompilerTest {

    val testSubject = DrainerCompiler()
    val setOfA = setOf("A")
    val setOfB = setOf("B")
    val setOfC = setOf("C")
    val setOfAAndB = setOf("A", "B")
    val setOfAA = setOf("AA")
    val setOfAB = setOf("AB")
    val setOfAC = setOf("AC")
    val setOfAAAndAB = setOf("AA", "AB")

    val collection = listOf(
        1 to setOfA,
        2 to setOfB,
        3 to setOfC,
        4 to setOfAAndB,
        5 to setOfAA,
        6 to setOfAB,
        7 to setOfAC,
        8 to setOfAAAndAB,
    )

    @Test
    fun compile_GivenSingleVariable() {
        val input = "A"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(1, 4)
    }

    @Test
    fun compile_GivenParamSingleVariable() {
        val input = "(A)"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(1, 4)
    }

    @Test
    fun compile_GivenParamNotSingleVariable() {
        val input = "(!A)"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(2, 3, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenNotSingleVariable() {
        val input = "!A"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(2, 3, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrVariable() {
        val input = "A|B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(1, 2, 4)
    }

    @Test
    fun compile_GivenAndVariable() {
        val input = "A&B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(4)
    }

    @Test
    fun compile_GivenOrNotVariable() {
        val input = "A|!B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) { it.second }.map { it.first }).containsExactly(1, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrNotBQuestionMarkVariable() {
        val input = "A|!B?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrNotAQuestionMarkVariable() {
        val input = "A|!A?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1, 2, 3, 4)
    }

    @Test
    fun compile_GivenOrNotAAsteriskVariable() {
        val input = "A|!A*"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1, 2, 3, 4)
    }

    @Test
    fun compile_GivenAndNotVariable() {
        val input = "A&!B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1)
    }

    @Test
    fun compile_GivenAsteriskVariable() {
        val input = "*"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenQuestionMarkVariable() {
        val input = "?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(1, 2, 3, 4)
    }

    @Test
    fun compile_GivenAQuestionMarkVariable() {
        val input = "A?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isFalse
        assertThat(compile.invoke(setOfAA)).isTrue
        assertThat(compile.invoke(setOfAB)).isTrue
        assertThat(compile.invoke(setOfAC)).isTrue
        assertThat(compile.invoke(setOfAAAndAB)).isTrue

        assertThat(collection.filter(input) {
            it.second
        }.map {
            it.first
        }).containsExactly(5, 6, 7, 8)
    }

}