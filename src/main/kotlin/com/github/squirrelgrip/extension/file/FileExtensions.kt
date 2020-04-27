package com.github.squirrelgrip.extension.file

import java.io.*

/**
 * Creates a FileWriter for the given File
 */
fun File.toWriter() = FileWriter(this)

/**
 * Creates a FileOutputStream for the given File
 */
fun File.toOutputStream() = FileOutputStream(this)

/**
 * Creates a FileReader for the given File
 */
fun File.toReader() = FileReader(this)

/**
 * Creates a FileInputStream for the given File
 */
fun File.toInputStream() = FileInputStream(this)


