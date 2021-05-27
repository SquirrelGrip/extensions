package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.extension.ExtendWith
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * This is a marker annotation that indicates the given test method or class is not expected
 * to call System.exit(). By annotating a test or a class with this annotation, we can prevent
 * an inadvertent System.exit() call from tearing down the test infrastructure.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS
)
@ExtendWith(
    SystemExitExtension::class
)
annotation class FailOnSystemExit 