package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.extension.ExtendWith

/**
 * This is a marker annotation that indicates the given test method or class is expected
 * to call System.exit() with a specific code
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SystemExitExtension::class)
annotation class ExpectSystemExitWithStatus(val value: Int)