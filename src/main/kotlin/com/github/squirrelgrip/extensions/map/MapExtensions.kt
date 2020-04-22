package com.github.squirrelgrip.extensions.map

/**
 * Reverses a map of K to V into a map of V to List<K>
 * @return reversed Map<V, List<K>>
 */
fun <K, V> Map<K, V>.reverseMany(): Map<V, List<K>> =
    toList()
        .groupBy { it.second }
        .mapValues { (_, value) ->
            value.map {
                it.first
            }
        }

/**
 * Reverse a map K to V into a map of K to V
 *
 * WARNING: This assumes a 1 to 1 mapping of keys to values, otherwise it will drop elements.
 * If you have 1 to N, use reverseMany() instead.
 *
 * @return reverse Map<K, V>
 */
fun <K, V> Map<K, V>.reverse(): Map<V, K> =
    entries.map { (k, v) ->  v to k}.toMap()