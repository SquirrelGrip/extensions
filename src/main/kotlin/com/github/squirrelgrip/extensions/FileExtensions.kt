package com.github.squirrelgrip.extensions

import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

fun File.toWriter() = FileWriter(this)

fun File.toStream() = FileOutputStream(this)


