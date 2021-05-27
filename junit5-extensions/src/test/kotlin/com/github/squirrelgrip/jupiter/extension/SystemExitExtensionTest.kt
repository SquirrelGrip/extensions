package com.github.squirrelgrip.jupiter.extension

import com.github.squirrelgrip.jupiter.extension.systemexit.ExpectSystemExit
import org.junit.jupiter.api.Test
import kotlin.system.exitProcess

internal class SystemExitExtensionTest {
    @Test
    @ExpectSystemExit
    fun expectSystemExitToReturn0() {
        exitProcess(0)
    }

    @Test
    @ExpectSystemExit
    fun expectSystemExit() {
        exitProcess(1)
    }
}