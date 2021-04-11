package com.github.squirrelgrip.extension.hash

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HashExtensionsTest {
    @Test
    fun hash() {
        assertThat("abcd".toHash()).isEqualTo(byteArrayOf(-30, -4, 113, 76, 71, 39, -18, -109, -107, -13, 36, -51, 46, 127, 51, 31))
        assertThat("abcd".md2Hash()).isEqualTo(byteArrayOf(-9, -112, -46, -93, -108, 13, -34, 64, 86, 56, 124, 120, 127, 48, 98, -8))
        assertThat("abcd".md5Hash()).isEqualTo(byteArrayOf(-30, -4, 113, 76, 71, 39, -18, -109, -107, -13, 36, -51, 46, 127, 51, 31))
        assertThat("abcd".sha1Hash()).isEqualTo(byteArrayOf(-127, -2, -117, -2, -121, 87, 108, 62, -53, 34, 66, 111, -114, 87, -124, 115, -126, -111, 122, -49))
        assertThat("abcd".sha224Hash()).isEqualTo(byteArrayOf(-89, 102, 84, -40, -29, 85, 14, -102, 45, 103, -96, -18, -74, -58, 123, 34, 14, 88, -123, -19, -35, 63, -34, 19, 88, 6, -26, 1))
        assertThat("abcd".sha256Hash()).isEqualTo(byteArrayOf(-120, -44, 38, 111, -44, -26, 51, -115, 19, -72, 69, -4, -14, -119, 87, -99, 32, -100, -119, 120, 35, -71, 33, 125, -93, -31, 97, -109, 111, 3, 21, -119))
        assertThat("abcd".sha384Hash()).isEqualTo(byteArrayOf(17, 101, -77, 64, 111, -16, -75, 42, 61, 36, 114, 31, 120, 84, 98, -54, 34, 118, -55, -12, 84, -95, 22, -62, -78, -70, 32, 23, 26, 121, 5, -22, 90, 2, 102, -126, -21, 101, -100, 77, 95, 17, 92, 54, 58, -93, -57, -101))
        assertThat("abcd".sha512Hash()).isEqualTo(byteArrayOf(-40, 2, 47, 32, 96, -83, 110, -3, 41, 122, -73, 61, -52, 83, 85, -55, -78, 20, 5, 75, 13, 23, 118, -95, 54, -90, 105, -46, 106, 125, 59, 20, -9, 58, -96, -48, -21, -1, 25, -18, 51, 51, 104, -16, 22, 75, 100, 25, -87, 109, -92, -98, 62, 72, 23, 83, -25, -23, 107, 113, 107, -36, -53, 111))
        assertThat("abcd".toHash("SHA")).isEqualTo(byteArrayOf(-127, -2, -117, -2, -121, 87, 108, 62, -53, 34, 66, 111, -114, 87, -124, 115, -126, -111, 122, -49))
    }

}