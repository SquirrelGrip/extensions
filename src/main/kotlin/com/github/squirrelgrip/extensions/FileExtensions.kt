package com.github.squirrelgrip.extensions

import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

inline fun File.toWriter() = FileWriter(this)

inline fun File.toStream() = FileOutputStream(this)


