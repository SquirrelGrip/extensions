package com.github.squirrelgrip.extension.process

import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class ProcessExtensionTest {
    @Test
    fun mapProcesses() {
        Runtime.getRuntime().exec("ps eww -A").map { it ->
            it
        }.forEach {
            println(it)
        }
    }

    @Test
    fun associateProcesses() {
        Runtime.getRuntime().exec("ps eww -A").associate { it ->
            val split = it.split("\\s+".toRegex(), 5)
            split[0] to split[4]
        }.forEach {
            println(it)
        }
    }
}

fun <T, U> Process.associate(f: (String) -> Pair<T, U>): Map<T, U> {
    val returnMap: MutableList<Pair<T, U>> = mutableListOf()
    BufferedReader(InputStreamReader(this.inputStream)).use {
        it.forEachLine { line ->
            returnMap.add(f(line))
        }
    }
    return returnMap.toMap()
}

fun <T> Process.map(f: (String) -> T): List<T> {
    val returnMap: MutableList<T> = mutableListOf()
    BufferedReader(InputStreamReader(this.inputStream)).use {
        it.forEachLine { line ->
            returnMap.add(f(line))
        }
    }
    return returnMap
}
