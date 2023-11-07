package com.github.squirrelgrip.extension.collection

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CompilerTest {

    val testSubject = FlatMapCollectionStringCompiler
    val setOfA = setOf("A")
    val setOfB = setOf("B")
    val setOfC = setOf("C")
    val setOfAAndB = setOf("A", "B")
    val setOfAA = setOf("AA")
    val setOfAB = setOf("AB")
    val setOfAC = setOf("AC")
    val setOfAAndAB = setOf("AA", "AB")

    val collection = listOf(
        1 to setOfA,
        2 to setOfB,
        3 to setOfC,
        4 to setOfAAndB,
        5 to setOfAA,
        6 to setOfAB,
        7 to setOfAC,
        8 to setOfAAndAB,
    )

    @Test
    fun compile_GivenSingleVariable() {
        val input = "A"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(1, 4)
    }

    @Test
    fun compile_GivenParamSingleVariable() {
        val input = "(A)"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(1, 4)
    }

    @Test
    fun compile_GivenParamNotSingleVariable() {
        val input = "(!A)"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(2, 3, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenNotSingleVariable() {
        val input = "!A"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(2, 3, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrVariable() {
        val input = "A|B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(1, 2, 4)
    }

    @Test
    fun compile_GivenAndVariable() {
        val input = "A&B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(4)
    }

    @Test
    fun compile_GivenOrNotVariable() {
        val input = "A|!B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(collection.flatMapFilterByExpression(input) { it.second }.map { it.first }).containsExactly(1, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrNotBQuestionMarkVariable() {
        val input = "A|!B?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrNotAQuestionMarkVariable() {
        val input = "A|!A?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4)
    }

    @Test
    fun compile_GivenAOrNotA() {
        val input = "A|!A"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenOrNotAAsteriskVariable() {
        val input = "A|!A*"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4)
    }

    @Test
    fun compile_GivenAndNotVariable() {
        val input = "A&!B"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAAndB)).isFalse

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1)
    }

    @Test
    fun compile_GivenAsteriskVariable() {
        val input = "*"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun compile_GivenQuestionMarkVariable() {
        val input = "?"
        val compile = testSubject.compile(input)
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAAndB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(1, 2, 3, 4)
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
        assertThat(compile.invoke(setOfAAndAB)).isTrue

        assertThat(
            collection.flatMapFilterByExpression(input) {
                it.second
            }.map {
                it.first
            }
        ).containsExactly(5, 6, 7, 8)
    }

    enum class TestEnum {
        A, B, C
    }

    data class TestClass(val value: String)

    val OBJECT_A = TestClass("A")
    val OBJECT_B = TestClass("B")
    val OBJECT_C = TestClass("C")

    val objectList = listOf(OBJECT_A, OBJECT_B, OBJECT_C)
    val objectArray = arrayOf(OBJECT_A, OBJECT_B, OBJECT_C)

    val stringList = listOf("A", "B", "C")
    val stringArray = arrayOf("A", "B", "C")

    @Test
    fun compile_GivenEnum() {
        assertThat("A".filterByExpression<TestEnum>()).containsExactly(TestEnum.A)
        assertThat("(A)".filterByExpression<TestEnum>()).containsExactly(TestEnum.A)
        assertThat("!A".filterByExpression<TestEnum>()).containsExactly(TestEnum.B, TestEnum.C)
        assertThat("!(A)".filterByExpression<TestEnum>()).containsExactly(TestEnum.B, TestEnum.C)
        assertThat("!A|A".filterByExpression<TestEnum>()).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
        assertThat("!A&A".filterByExpression<TestEnum>()).isEmpty()
        assertThat("(!A|B)|A".filterByExpression<TestEnum>()).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
        assertThat("X".filterByExpression<TestEnum>()).isEmpty()
        assertThat("!X".filterByExpression<TestEnum>()).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
    }

    @Test
    fun compile_GivenEnumWithExtraExpressions() {
        assertThat("A".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.A)
        assertThat("(A)".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.A)
        assertThat("!A".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.B, TestEnum.C)
        assertThat("!(A)".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.B, TestEnum.C)
        assertThat("!A|A".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
        assertThat("!A&A".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).isEmpty()
        assertThat("(!A|B)|A".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
        assertThat("X".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.A, TestEnum.B)
        assertThat("!X".filterByExpression<TestEnum>(mapOf("X" to "A|B"))).containsExactly(TestEnum.C)

        assertThat("ALL".filterByExpression<TestEnum>(mapOf("ALL" to "A|B|C"))).containsExactly(TestEnum.A, TestEnum.B, TestEnum.C)
    }

    @Test
    fun collectionFilter() {
        assertThat(objectList.mapFilterByExpression("A") { it.value }).containsExactly(OBJECT_A)
        assertThat(objectList.mapFilterByExpression("B") { it.value }).containsExactly(OBJECT_B)
        assertThat(objectList.mapFilterByExpression("C") { it.value }).containsExactly(OBJECT_C)
        assertThat(objectList.mapFilterByExpression("!A") { it.value }).containsExactly(OBJECT_B, OBJECT_C)

        assertThat(objectList.mapFilterByExpression("") { it.value }).isEmpty()
        assertThat(objectList.mapFilterByExpression(null) { it.value }).containsAll(objectList)

        assertThat(objectList.mapFilterByExpression("ALL", mapOf("ALL" to "A|B|C"))  { it.value }).containsAll(objectList)
    }

    @Test
    fun arrayFilter() {
        assertThat(objectArray.mapFilterByExpression("A") { it.value }).containsExactly(OBJECT_A)
        assertThat(objectArray.mapFilterByExpression("B") { it.value }).containsExactly(OBJECT_B)
        assertThat(objectArray.mapFilterByExpression("C") { it.value }).containsExactly(OBJECT_C)
        assertThat(objectArray.mapFilterByExpression("!A") { it.value }).containsExactly(OBJECT_B, OBJECT_C)

        assertThat(objectArray.mapFilterByExpression("") { it.value }).isEmpty()
        assertThat(objectArray.mapFilterByExpression(null) { it.value }).containsAll(objectList)

        assertThat(objectArray.mapFilterByExpression("ALL", mapOf("ALL" to "A|B|C")) { it.value }).containsAll(objectList)
    }

    @Test
    fun stringCollectionFilter() {
        assertThat(stringList.filterByExpression("A")).containsExactly("A")
        assertThat(stringList.filterByExpression("B")).containsExactly("B")
        assertThat(stringList.filterByExpression("C")).containsExactly("C")
        assertThat(stringList.filterByExpression("!A")).containsExactly("B", "C")

        assertThat(stringList.filterByExpression("")).isEmpty()
        assertThat(stringList.filterByExpression(null)).containsAll(stringList)

        assertThat(stringList.filterByExpression("ALL", mapOf("ALL" to "A|B|C"))).containsAll(stringList)
    }

    @Test
    fun stringArrayFilter() {
        assertThat(stringArray.filterByExpression("A")).containsExactly("A")
        assertThat(stringArray.filterByExpression("B")).containsExactly("B")
        assertThat(stringArray.filterByExpression("C")).containsExactly("C")
        assertThat(stringArray.filterByExpression("!A")).containsExactly("B", "C")
        assertThat(stringArray.filterByExpression("B|C")).containsExactly("B", "C")

        assertThat(stringArray.filterByExpression("")).isEmpty()
        assertThat(stringArray.filterByExpression(null)).containsAll(stringList)

        assertThat(stringArray.filterByExpression("ALL", mapOf("ALL" to "A|B|C"))).containsAll(stringList)
    }

    @Test
    fun stringStreamFilter() {
        assertThat(stringList.asSequence().filterByExpression("A").toList()).containsExactly("A")
        assertThat(stringList.asSequence().filterByExpression("B").toList()).containsExactly("B")
        assertThat(stringList.asSequence().filterByExpression("C").toList()).containsExactly("C")
        assertThat(stringList.asSequence().filterByExpression("!A").toList()).containsExactly("B", "C")

        assertThat(stringList.asSequence().filterByExpression("").toList()).isEmpty()
        assertThat(stringList.asSequence().filterByExpression(null).toList()).containsAll(stringList)

        assertThat(stringList.asSequence().filterByExpression("ALL", mapOf("ALL" to "A|B|C")).toList()).containsAll(stringList)
    }
}
