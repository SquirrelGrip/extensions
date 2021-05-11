package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SystemExitExtension::class)
annotation class ExpectSystemExit(val value: Int)