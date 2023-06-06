package com.github.squirrelgrip.extension.collection

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.util.*

object MapStringCompiler : Compiler<String>() {
    override fun matchesRegex(
        regExString: String,
        candidate: String
    ): Boolean =
        globToRegEx(regExString).matches(candidate)

    override fun matches(
        control: String,
        candidate: String
    ): Boolean =
        control in candidate
}

object FlatMapCollectionStringCompiler : Compiler<Collection<String>>() {
    override fun matchesRegex(
        regExString: String,
        candidate: Collection<String>
    ): Boolean =
        globToRegEx(regExString).let { regex ->
            candidate.any {
                regex.matches(it)
            }
        }

    override fun matches(
        control: String,
        candidate: Collection<String>
    ): Boolean =
        control in candidate
}

object FlatMapSequenceStringCompiler : Compiler<Sequence<String>>() {
    override fun matchesRegex(
        regExString: String,
        candidate: Sequence<String>
    ): Boolean =
        globToRegEx(regExString).let { regex ->
            candidate.any {
                regex.matches(it)
            }
        }

    override fun matches(
        control: String,
        candidate: Sequence<String>
    ): Boolean =
        control in candidate
}

abstract class Compiler<T> {
    val visitor: DrainerBaseVisitor<(T) -> Boolean> =
        object : DrainerBaseVisitor<(T) -> Boolean>() {
            override fun visitAndExpression(ctx: DrainerParser.AndExpressionContext): (T) -> Boolean =
                {
                    visit(ctx.expression(0)).invoke(it) && visit(ctx.expression(1)).invoke(it)
                }

            override fun visitOrExpression(ctx: DrainerParser.OrExpressionContext): (T) -> Boolean =
                {
                    visit(ctx.expression(0)).invoke(it) || visit(ctx.expression(1)).invoke(it)
                }

            override fun visitNotExpression(ctx: DrainerParser.NotExpressionContext): (T) -> Boolean =
                {
                    !visit(ctx.expression()).invoke(it)
                }

            override fun visitVariableExpression(ctx: DrainerParser.VariableExpressionContext): (T) -> Boolean =
                {
                    matches(ctx.variable().text, it)
                }

            override fun visitWildVariableExpression(ctx: DrainerParser.WildVariableExpressionContext): (T) -> Boolean =
                {
                    matchesRegex(ctx.wildVariable().text, it)
                }

            override fun visitParenExpression(ctx: DrainerParser.ParenExpressionContext): (T) -> Boolean =
                {
                    visit(ctx.expression()).invoke(it)
                }

            override fun visitPredicate(ctx: DrainerParser.PredicateContext): (T) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }
        }

    abstract fun matchesRegex(
        regExString: String,
        candidate: T
    ): Boolean

    abstract fun matches(
        control: String,
        candidate: T
    ): Boolean

    fun globToRegEx(glob: String): Regex {
        var out: String = "^"
        glob.forEach { c ->
            out += when (c) {
                '*' -> ".*"
                '?' -> '.'
                '.' -> "\\."
                else -> c
            }
        }
        out += '$'
        return out.toRegex()
    }

    fun compile(expression: String): (T) -> Boolean =
        if (expression.isNotBlank()) {
            visitor.visit(
                DrainerParser(
                    CommonTokenStream(
                        DrainerLexer(
                            CharStreams.fromString(expression)
                        )
                    )
                ).predicate()
            )
        } else {
            { false }
        }

    fun <V> invoke(
        collection: Collection<V>,
        expression: String?,
        extra: Map<String, String> = emptyMap(),
        converter: (V) -> T
    ): List<V> =
        expression.prepare(extra)?.let { expression ->
            compile(expression).let { predicate ->
                collection.filter {
                    predicate.invoke(converter.invoke(it))
                }
            }
        } ?: collection.toList()

    fun <V> invoke(
        array: Array<V>,
        expression: String?,
        extra: Map<String, String> = emptyMap(),
        transform: (V) -> T
    ): List<V> =
        expression.prepare(extra)?.let { expression ->
            compile(expression).let { predicate ->
                array.filter {
                    predicate.invoke(transform.invoke(it))
                }
            }
        } ?: array.toList()

    fun <V> invoke(
        sequence: Sequence<V>,
        expression: String?,
        extra: Map<String, String> = emptyMap(),
        transform: (V) -> T
    ): Sequence<V> =
        expression.prepare(extra)?.let { expression ->
            compile(expression).let { predicate ->
                sequence.filter {
                    predicate.invoke(transform.invoke(it))
                }
            }
        } ?: sequence

    private fun String?.prepare(extra: Map<String, String>): String? =
        this?.let {
            extra.asSequence().fold(it) { expression, (variable, value) ->
                expression.replace(variable, "(${value})")
            }
        }
}

inline fun <reified E : Enum<E>> String?.filterByExpression(
    extra: Map<String, String> = emptyMap()
): EnumSet<E> =
    MapStringCompiler.invoke(enumValues<E>(), this, extra) { it.name }.toEnumSet()

fun <T> Collection<T>.flatMapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> Collection<String> = { setOf(it.toString()) }
): List<T> =
    FlatMapCollectionStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun <T> Array<T>.flatMapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> Collection<String> = { setOf(it.toString()) }
): List<T> =
    FlatMapCollectionStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun <T> Sequence<T>.flatMapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> Sequence<String> = { sequenceOf(it.toString()) }
): Sequence<T> =
    FlatMapSequenceStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun <T> Collection<T>.mapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> String = { it.toString() }
): List<T> =
    MapStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun <T> Array<T>.mapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> String = { it.toString() }
): List<T> =
    MapStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun <T> Sequence<T>.mapFilterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap(),
    transform: (T) -> String = { it.toString() }
): Sequence<T> =
    MapStringCompiler.invoke(
        this,
        expression,
        extra,
        transform
    )

fun Collection<String>.filterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap()
): List<String> =
    MapStringCompiler.invoke(
        this,
        expression,
        extra
    ) { it }

fun Array<String>.filterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap()
): List<String> =
    MapStringCompiler.invoke(
        this,
        expression,
        extra
    ) { it }

fun Sequence<String>.filterByExpression(
    expression: String?,
    extra: Map<String, String> = emptyMap()
): Sequence<String> =
    FlatMapSequenceStringCompiler.invoke(
        this,
        expression,
        extra
    ) { sequenceOf(it) }

inline fun <reified E : Enum<E>> Collection<E>?.toEnumSet(): EnumSet<E> =
    if (this == null) {
        EnumSet.allOf(E::class.java)
    } else if (this.isEmpty()) {
        EnumSet.noneOf(E::class.java)
    } else {
        EnumSet.copyOf(this)
    }