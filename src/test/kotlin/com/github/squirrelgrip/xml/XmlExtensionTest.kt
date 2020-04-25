package com.github.squirrelgrip.xml

import com.github.squirrelgrip.Sample
import com.github.squirrelgrip.extensions.xml.toInstance
import com.github.squirrelgrip.extensions.xml.toXml
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class XmlExtensionTest {
    @Test
    fun `toXml`() {
        assertThat(Sample().toXml()).isEqualTo("<Sample><v>0</v><s>A Simple String</s><m><a>AAA</a></m><l><l>1</l><l>AAA</l></l></Sample>")
        assertThat("<Sample><v>0</v><s>A Simple String</s><m><a>AAA</a></m><l><l>1</l><l>AAA</l></l></Sample>".toInstance<Sample>()).isEqualTo(Sample())
    }
}