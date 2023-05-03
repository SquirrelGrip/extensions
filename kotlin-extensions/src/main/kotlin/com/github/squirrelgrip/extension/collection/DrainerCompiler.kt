package com.github.squirrelgrip.extension.collection

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.util.*

object StringDrainerCompiler : DrainerCompiler<Collection<String>>() {
    override fun matches(
        glob: String,
        it: Collection<String>
    ): Boolean {
        val regex = globToRegEx(glob)
        return it.any { value ->
            regex.matches(value)
        }
    }

    override fun contains(
        value: String,
        collection: Collection<String>
    ): Boolean =
        value in collection
}

abstract class DrainerCompiler<T>() {
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
                    contains(ctx.variable().text, it)
                }

            override fun visitWildVariableExpression(ctx: DrainerParser.WildVariableExpressionContext): (T) -> Boolean =
                {
                    matches(ctx.wildVariable().text, it)
                }

            override fun visitParenExpression(ctx: DrainerParser.ParenExpressionContext): (T) -> Boolean =
                {
                    visit(ctx.expression()).invoke(it)
                }

            override fun visitPredicate(ctx: DrainerParser.PredicateContext): (T) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }
        }

    abstract fun matches(
        regExString: String,
        it: T
    ): Boolean

    abstract fun contains(
        value: String,
        collection: T
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

    inline fun compile(expression: String): (T) -> Boolean =
        visitor.visit(
            DrainerParser(
                CommonTokenStream(
                    DrainerLexer(
                        CharStreams.fromString(expression)
                    )
                )
            ).predicate()
        )
}

inline fun <reified E : Enum<E>> String?.filter(): Set<E> =
    this?.let {
        val predicate = StringDrainerCompiler.compile(this)
        val filter = enumValues<E>().toList().filter {
            predicate.invoke(setOf(it.name))
        }
        filter.toSet()
    } ?: emptySet()


inline fun <reified E : Enum<E>> String?.filter(extra: Map<String, String>): EnumSet<E> =
    this?.let {
        var expression = it
        extra.forEach { (variable, value) ->
            expression = expression.replace(variable, "(${value})")
        }
        enumValues<E>().filter(expression) {
            setOf(it.name)
        }
    }.toEnumSet()

fun <T> Collection<T>.filter(expression: String, converter: (T) -> Collection<String>): List<T> =
    StringDrainerCompiler.compile(expression).let { predicate ->
        this.filter {
            predicate.invoke(converter.invoke(it))
        }
    }

fun <T> Array<T>.filter(expression: String, converter: (T) -> Collection<String>): List<T> =
    StringDrainerCompiler.compile(expression).let { predicate ->
        this.filter {
            predicate.invoke(converter.invoke(it))
        }
    }

fun Collection<Collection<String>>.filter(expression: String): List<Collection<String>> {
    val predicate = StringDrainerCompiler.compile(expression)
    return this.filter {
        predicate.invoke(it)
    }
}

inline fun <reified E : Enum<E>> Collection<E>?.toEnumSet(): EnumSet<E> =
    if (this.isNullOrEmpty()) {
        EnumSet.noneOf(E::class.java)
    } else {
        EnumSet.copyOf(this)
    }