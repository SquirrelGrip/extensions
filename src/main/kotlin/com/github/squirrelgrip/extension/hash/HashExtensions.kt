package com.github.squirrelgrip.extension.hash

import java.security.MessageDigest

fun String.md2Hash(): String {
    return this.toHash("MD2")
}

fun String.md5Hash(): String {
    return this.toHash("MD5")
}

fun String.sha1Hash(): String {
    return this.toHash("SHA")
}

fun String.sha224Hash(): String {
    return this.toHash("SHA-224")
}

fun String.sha256Hash(): String {
    return this.toHash("SHA-256")
}

fun String.sha384Hash(): String {
    return this.toHash("SHA-384")
}

fun String.sha512Hash(): String {
    return this.toHash("SHA-512")
}

fun String.toHash(algorithm: String = "MD5"): String {
    val messageDigest = MessageDigest.getInstance(algorithm)
    messageDigest.update(this.toByteArray())
    val bytes = messageDigest.digest()
    return bytes.toHexString()

}

fun ByteArray.toHexString() : String {
    return this.joinToString("") { it.toString(16) }
}