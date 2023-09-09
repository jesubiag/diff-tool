package com.difftool

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class DiffToolTest {

    @Test
    fun `should return empty for two null objects`() {
        val previous: SimplePerson? = null
        val current: SimplePerson? = null

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return empty when both objects are the same`() {
        val previous = SimplePersonMother.personJohn()
        val current = previous

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return empty when current object did not change`() {
        val previous = SimplePersonMother.personJohn()
        val current = SimplePersonMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return all not null object properties when previous is not null and current is null`() {
        val previous = SimplePersonMother.personJohn()
        val current = null

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(SimplePersonMother.Properties.Id, "1", "null"),
            PropertyUpdate(SimplePersonMother.Properties.Name, "John", "null"),
            PropertyUpdate(SimplePersonMother.Properties.Age, "40", "null")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun `should return all not null object properties when previous is null and current is not null`() {
        val previous = null
        val current = SimplePersonMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(SimplePersonMother.Properties.Id, "null", "1"),
            PropertyUpdate(SimplePersonMother.Properties.Name, "null", "John"),
            PropertyUpdate(SimplePersonMother.Properties.Age, "null", "40")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun `should return list of changes on two non nested objects`() {
        val previous = SimplePersonMother.personJohn()
        val current = SimplePersonMother.personJohnOlder()

        val diff = DiffTool.diff(previous, current)

        val newAgeProperty = PropertyUpdate(SimplePersonMother.Properties.Age, "40", "50")
        assertThat(diff, containsInAnyOrder(newAgeProperty))
    }


}