package com.github.squirrelgrip.extensions.json

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Converts Any to a JSON String representation
 */
fun Any.toJson() = ObjectMapper().writeValueAsString(this)