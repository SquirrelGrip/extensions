package com.github.squirrelgrip.jupiter.extension.systemout

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.UnsupportedEncodingException

//import org.junit.runners.model.Statement;
class LogPrintStream(private val printStreamHandler: PrintStreamHandler) {
    private val muteableLogStream: MuteableLogStream

    //	public Statement createStatement(final Statement base) {
    //		return new Statement() {
    //			@Override
    //			public void evaluate() throws Throwable {
    //				try {
    //					printStreamHandler.createRestoreStatement(new Statement() {
    //						@Override
    //						public void evaluate() throws Throwable {
    //							printStreamHandler.replaceCurrentStreamWithOutputStream(muteableLogStream);
    //							base.evaluate();
    //						}
    //					}).evaluate();
    //				} catch (Throwable e) {
    //					muteableLogStream.failureLog.writeTo(printStreamHandler.getStream());
    //					throw e;
    //				}
    //			}
    //		};
    //	}
    fun clearLog() {
        muteableLogStream.log.reset()
    }

    fun enableLog() {
        muteableLogStream.logMuted = false
    }

    /* The MuteableLogStream is created with the default encoding
		 * because it writes to System.out or System.err if not muted and
		 * System.out/System.err uses the default encoding. As a result all
		 * other streams receive input that is encoded with the default
		 * encoding.
		 */
    val log: String
        get() {
            /* The MuteableLogStream is created with the default encoding
		 * because it writes to System.out or System.err if not muted and
		 * System.out/System.err uses the default encoding. As a result all
		 * other streams receive input that is encoded with the default
		 * encoding.
		 */
            val encoding = System.getProperty("file.encoding")
            return try {
                muteableLogStream.log.toString(encoding)
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        }
    val logWithNormalizedLineSeparator: String
        get() {
            val lineSeparator = System.getProperty("line.separator")
            return log.replace(lineSeparator, "\n")
        }
    val logAsBytes: ByteArray
        get() = muteableLogStream.log.toByteArray()

    fun mute() {
        muteableLogStream.originalStreamMuted = true
    }

    fun muteForSuccessfulTests() {
        mute()
        muteableLogStream.failureLogMuted = false
    }

    private class MuteableLogStream internal constructor(val originalStream: OutputStream?) : OutputStream() {
        val failureLog = ByteArrayOutputStream()
        val log = ByteArrayOutputStream()
        var originalStreamMuted = false
        var failureLogMuted = true
        var logMuted = true
        @Throws(IOException::class)
        override fun write(b: Int) {
            if (!originalStreamMuted) originalStream!!.write(b)
            if (!failureLogMuted) failureLog.write(b)
            if (!logMuted) log.write(b)
        }

        @Throws(IOException::class)
        override fun flush() {
            originalStream!!.flush()
            //ByteArrayOutputStreams don't have to be closed
        }

        @Throws(IOException::class)
        override fun close() {
            originalStream!!.close()
            //ByteArrayOutputStreams don't have to be closed
        }
    }

    init {
        muteableLogStream = MuteableLogStream(printStreamHandler.stream)
    }
}