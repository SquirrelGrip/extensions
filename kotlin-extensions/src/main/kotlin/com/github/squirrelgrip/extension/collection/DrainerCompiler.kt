package com.github.squirrelgrip.extension.collection

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class DrainerCompiler {

    companion object {
        val visitor: DrainerBaseVisitor<(Collection<String>) -> Boolean> = object : DrainerBaseVisitor<(Collection<String>) -> Boolean>() {
            override fun visitAndExpression(ctx: DrainerParser.AndExpressionContext): (Collection<String>) -> Boolean = {
                visit(ctx.expression(0)).invoke(it) && visit(ctx.expression(1)).invoke(it)
            }

            override fun visitOrExpression(ctx: DrainerParser.OrExpressionContext): (Collection<String>) -> Boolean = {
                visit(ctx.expression(0)).invoke(it) || visit(ctx.expression(1)).invoke(it)
            }

            override fun visitNotExpression(ctx: DrainerParser.NotExpressionContext): (Collection<String>) -> Boolean = {
                !visit(ctx.expression()).invoke(it)
            }

            override fun visitVariableExpression(ctx: DrainerParser.VariableExpressionContext): (Collection<String>) -> Boolean = {
                ctx.variable().text in it
            }

            override fun visitParenExpression(ctx: DrainerParser.ParenExpressionContext): (Collection<String>) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }

            override fun visitPredicate(ctx: DrainerParser.PredicateContext): (Collection<String>) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }
        }
    }

    fun compile(input: String): (Collection<String>) -> Boolean =
        visitor.visit(DrainerParser(CommonTokenStream(DrainerLexer(CharStreams.fromString(input)))).predicate())

}

fun <T> Collection<T>.filter(expression: String, extractor: (T) -> Collection<String>): List<T> {
    val predicate = DrainerCompiler().compile(expression)
    return this.filter {
        predicate.invoke(extractor.invoke(it))
    }
}
