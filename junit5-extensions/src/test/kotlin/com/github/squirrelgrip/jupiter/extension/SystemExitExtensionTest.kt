package com.github.squirrelgrip.jupiter.extension

import com.github.squirrelgrip.jupiter.extension.systemexit.ExpectSystemExit
import org.junit.jupiter.api.Test

internal class SystemExitExtensionTest {
    @Test
    @ExpectSystemExit(0)
    fun expectSystemExitToReturn0() {
        System.exit(0)
    }

    @Test
    @ExpectSystemExit(1)
    fun expectSystemExit() {
        System.exit(1)
    }
}