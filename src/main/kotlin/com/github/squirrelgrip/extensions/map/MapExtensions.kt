package com.github.squirrelgrip.extensions.map

/**
 * Reverses a List<Pair<K, V>> into a Map<V, List<K>>
 * @return reversed Map<V, List<K>>
 */
fun <K, V> Iterable<Pair<K, V>>.swap(): Map<V, List<K>> =
    groupBy { it.second }.mapValues { (_, value) ->
        value.map {
            it.first
        }
    }

/**
 * Reverses a map of K to V into a map of V to List<K>
 * @return reversed Map<V, List<K>>
 */
fun <K, V> Map<K, V>.swap(): Map<V, List<K>> =
    toList().swap()

/**
 * Reverses a map of K to Iterable<V> into a map of V to List<K>
 * @return reversed Map<V, List<K>>
 */
fun <K, V> Map<K, Iterable<V>>.swapWithCollection(): Map<V, List<K>> =
    entries.flatMap { e ->
        e.value.map { e.key to it }
    }.swap()
