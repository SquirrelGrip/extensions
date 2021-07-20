package com.github.squirrelgrip.jupiter.extension.systemexit

class SystemExitPreventedException(
    val statusCode: Int
) : SecurityException()
