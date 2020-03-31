package com.github.squirrelgrip.extensions.io

import java.io.*
import java.nio.charset.Charset
import javax.swing.text.ChangedCharSetException

fun Reader.toBufferedReader(): BufferedReader {
    return BufferedReader(this)
}

fun Writer.toBufferedWriter(): BufferedWriter {
    return BufferedWriter(this)
}

fun Writer.toPrintWriter(): PrintWriter {
    return PrintWriter(this)
}

fun OutputStream.toWriter(charset: Charset = Charset.defaultCharset()): Writer {
    return OutputStreamWriter(this, charset)
}

fun InputStream.toReader(charset: Charset = Charset.defaultCharset()): Reader {
    return InputStreamReader(this, charset)
}