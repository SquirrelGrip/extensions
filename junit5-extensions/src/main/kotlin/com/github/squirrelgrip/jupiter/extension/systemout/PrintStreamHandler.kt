package com.github.squirrelgrip.jupiter.extension.systemout

import java.io.OutputStream
import java.io.PrintStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

//import org.junit.runners.model.Statement;
enum class PrintStreamHandler {
    SYSTEM_OUT {
        override val stream: PrintStream
            get() {
                return System.out
            }

        override fun replaceCurrentStreamWithPrintStream(stream: PrintStream?) {
            System.setOut(stream)
        }
    },
    SYSTEM_ERR {
        override val stream: PrintStream
            get() {
                return System.err
            }

        override fun replaceCurrentStreamWithPrintStream(stream: PrintStream?) {
            System.setErr(stream)
        }
    };

    //	Statement createRestoreStatement(final Statement base) {
    //		return new Statement() {
    //			@Override
    //			public void evaluate() throws Throwable {
    //				PrintStream originalStream = getStream();
    //				try {
    //					base.evaluate();
    //				} finally {
    //					replaceCurrentStreamWithPrintStream(originalStream);
    //				}
    //			}
    //		};
    //	}
    @Throws(UnsupportedEncodingException::class)
    fun replaceCurrentStreamWithOutputStream(outputStream: OutputStream?) {
        val printStream = PrintStream(
            outputStream, AUTO_FLUSH, DEFAULT_ENCODING
        )
        replaceCurrentStreamWithPrintStream(printStream)
    }

    abstract val stream: PrintStream?
    abstract fun replaceCurrentStreamWithPrintStream(stream: PrintStream?)

    companion object {
        private const val AUTO_FLUSH = true
        private val DEFAULT_ENCODING = Charset.defaultCharset().name()
    }
}