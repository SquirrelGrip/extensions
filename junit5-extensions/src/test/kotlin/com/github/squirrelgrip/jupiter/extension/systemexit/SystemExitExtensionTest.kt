package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.RetryingTest
import kotlin.system.exitProcess

internal class SystemExitExtensionTest {
    companion object {
        var run = 0
    }

    @Test
    @ExpectSystemExit
    fun expectSystemExitToReturn0() {
        exitProcess(0)
    }

    @Test
    @ExpectSystemExit
    fun expectSystemExitReturn1() {
        exitProcess(1)
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    fun expectSystemExitWithStatus1() {
        exitProcess(1)
    }

}