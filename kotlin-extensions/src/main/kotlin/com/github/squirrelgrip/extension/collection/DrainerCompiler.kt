package com.github.squirrelgrip.extension.collection

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.util.*

class DrainerCompiler {

    companion object {
        val visitor: DrainerBaseVisitor<(Collection<String>) -> Boolean> =
            object : DrainerBaseVisitor<(Collection<String>) -> Boolean>() {
                override fun visitAndExpression(ctx: DrainerParser.AndExpressionContext): (Collection<String>) -> Boolean =
                    {
                        visit(ctx.expression(0)).invoke(it) && visit(ctx.expression(1)).invoke(it)
                    }

                override fun visitOrExpression(ctx: DrainerParser.OrExpressionContext): (Collection<String>) -> Boolean =
                    {
                        visit(ctx.expression(0)).invoke(it) || visit(ctx.expression(1)).invoke(it)
                    }

                override fun visitNotExpression(ctx: DrainerParser.NotExpressionContext): (Collection<String>) -> Boolean =
                    {
                        !visit(ctx.expression()).invoke(it)
                    }

                override fun visitNotVariableExpression(ctx: DrainerParser.NotVariableExpressionContext): (Collection<String>) -> Boolean =
                    {
                        ctx.variable().text !in it
                    }

                override fun visitWildVariableExpression(ctx: DrainerParser.WildVariableExpressionContext): (Collection<String>) -> Boolean =
                    {
                        val regex = globToRegEx(ctx.wildVariable().text)
                        it.any { value ->
                            regex.matches(value)
                        }
                    }

                override fun visitVariableExpression(ctx: DrainerParser.VariableExpressionContext): (Collection<String>) -> Boolean =
                    {
                        ctx.variable().text in it
                    }

                override fun visitParenExpression(ctx: DrainerParser.ParenExpressionContext): (Collection<String>) -> Boolean =
                    {
                        visit(ctx.expression()).invoke(it)
                    }

                override fun visitPredicate(ctx: DrainerParser.PredicateContext): (Collection<String>) -> Boolean = {
                    visit(ctx.expression()).invoke(it)
                }
            }

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
    }

    inline fun compile(input: String): (Collection<String>) -> Boolean =
        visitor.visit(DrainerParser(CommonTokenStream(DrainerLexer(CharStreams.fromString(input)))).predicate())
}

inline fun <reified E : Enum<E>> String?.filter(): Set<E> =
    this?.let {
        val predicate = DrainerCompiler().compile(this)
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

inline fun <reified E: Enum<E>> Collection<E>?.toEnumSet(): EnumSet<E> =
    this?.let {
        if (it.isEmpty()) {
            EnumSet.noneOf(E::class.java)
        } else {
            EnumSet.copyOf(it)
        }
    } ?: EnumSet.noneOf(E::class.java)

fun <T> Collection<T>.filter(expression: String, extractor: (T) -> Collection<String>): List<T> =
    DrainerCompiler().compile(expression).let { predicate ->
        this.filter {
            predicate.invoke(extractor.invoke(it))
        }
    }

fun <T> Array<T>.filter(expression: String, extractor: (T) -> Collection<String>): List<T> =
    DrainerCompiler().compile(expression).let { predicate ->
        this.filter {
            predicate.invoke(extractor.invoke(it))
        }
    }

fun Collection<Collection<String>>.filter(expression: String): List<Collection<String>> {
    val predicate = DrainerCompiler().compile(expression)
    return this.filter {
        predicate.invoke(it)
    }
}
