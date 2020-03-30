package com.github.squirrelgrip.extensions.file

import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * Creates a FileWriter for the given File
 */
fun File.toWriter() = FileWriter(this)

/**
 * Creates a FileOutputStream for the given File
 */
fun File.toStream() = FileOutputStream(this)


