package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.junit.platform.commons.support.AnnotationSupport.findAnnotation
import java.util.*

/**
 * Verify that System.exit() was called without actually calling it.
 */
class SystemExitExtension :
    BeforeEachCallback, AfterEachCallback, TestExecutionExceptionHandler {

    private var expectedStatusCode: Int? = null
    private val disallowExitSecurityManager: DisallowExitSecurityManager =
        DisallowExitSecurityManager(System.getSecurityManager())
    private var originalSecurityManager: SecurityManager? = null

    override fun afterEach(context: ExtensionContext?) {
        System.setSecurityManager(originalSecurityManager)
        if (expectedStatusCode == null) {
            assertNotNull(
                disallowExitSecurityManager.firstExitStatusCode,
                "Expected System.exit() to be called, but it was not"
            )
        } else {
            assertEquals(
                expectedStatusCode,
                disallowExitSecurityManager.firstExitStatusCode,
                "Expected System.exit($expectedStatusCode) to be called, but it was not."
            )
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        originalSecurityManager = System.getSecurityManager()
        val annotation: ExpectSystemExit? = getAnnotation(
            context,
            ExpectSystemExit::class.java
        )
        if (annotation != null) {
            expectedStatusCode = annotation.value
        }
        System.setSecurityManager(disallowExitSecurityManager)
    }

    /**
     * This is here so we can catch exceptions thrown by our own security manager and prevent them from
     * stopping the annotated test. If anything other than our own exception comes through, throw it because
     * the system SecurityManager to which we delegate prevented some other action from happening.
     *
     * @param context   the current extension context; never `null`
     * @param throwable the `Throwable` to handle; never `null`
     * @throws Throwable if the throwable argument is not a SystemExitPreventedException
     */
    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        if (throwable !is SystemExitPreventedException) {
            throw throwable
        }
    }

    private fun <T : Annotation> getAnnotation(context: ExtensionContext, annotationClass: Class<T>): T? {
        val method: Optional<T> = findAnnotation(context.testMethod, annotationClass)
        return if (method.isPresent) {
            method.get()
        } else {
            val clazz: Optional<T> = findAnnotation(context.testClass, annotationClass)
            if ( clazz.isPresent) clazz.get() else null
        }
    }
}