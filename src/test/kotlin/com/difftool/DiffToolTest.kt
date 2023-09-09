package com.difftool

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class DiffToolTest {

    @Test
    fun `should return empty for two null objects`() {
        val o1 = null
        val o2 = null

        val diff = DiffTool.diff(o1, o2)

        assertThat(diff, empty())
    }

    @Test
    fun `should return empty when both objects are the same`() {
        val o1 = SimplePerson(1, "John", 40)
        val o2 = o1

        val diff = DiffTool.diff(o1, o2)

        assertThat(diff, empty())
    }

}