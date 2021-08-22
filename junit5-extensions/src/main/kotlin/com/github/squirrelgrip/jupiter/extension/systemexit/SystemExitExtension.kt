package com.github.squirrelgrip.jupiter.extension.systemexit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.*
import org.junit.platform.commons.support.AnnotationSupport.findAnnotation
import java.util.*

/**
 * Does the work of installing the DisallowExitSecurityManager, interpreting the test results, and
 * returning the original SecurityManager to service.
 */
class SystemExitExtension : BeforeEachCallback, AfterEachCallback, TestExecutionExceptionHandler {
    private var expectedStatusCode: Int? = null
    private var failOnSystemExit = false
    private val disallowExitSecurityManager = DisallowExitSecurityManager(System.getSecurityManager())
    private var originalSecurityManager: SecurityManager? = null

    override fun afterEach(context: ExtensionContext) {
        // Return the original SecurityManager, if any, to service.
        System.setSecurityManager(originalSecurityManager)
        if (failOnSystemExit) {
            assertEquals(
                0,
                disallowExitSecurityManager.preventedSystemExitCount,
                "Unexpected System.exit(" + disallowExitSecurityManager.firstExitStatusCode + ") caught"
            )
        } else if (expectedStatusCode == null) {
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
        // Set aside the current SecurityManager
        originalSecurityManager = System.getSecurityManager()

        // Should we fail on a System.exit() rather than letting it bubble out?
        failOnSystemExit = getAnnotation(context, FailOnSystemExit::class.java).isPresent

        // Get the expected exit status code, if any
        getAnnotation(context, ExpectSystemExitWithStatus::class.java).ifPresent { code ->
            expectedStatusCode = code.value
        }

        // Install our own SecurityManager
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

    // Find the annotation on a method, or failing that, a class.
    private fun <T : Annotation?> getAnnotation(context: ExtensionContext, annotationClass: Class<T>): Optional<T> {
        val method: Optional<T> = findAnnotation(context.testMethod, annotationClass)
        return if (method.isPresent) {
            method
        } else {
            findAnnotation(context.testClass, annotationClass)
        }
    }
}