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
        val previous = ObjectsMother.personJohn()
        val current = previous

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return empty when current object did not change`() {
        val previous = ObjectsMother.personJohn()
        val current = ObjectsMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return all not null object properties when previous is not null and current is null`() {
        val previous = ObjectsMother.personJohn()
        val current = null

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.Id, "1", "null"),
            PropertyUpdate(ObjectsMother.Properties.Name, "John", "null"),
            PropertyUpdate(ObjectsMother.Properties.Age, "40", "null")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun `should return all not null object properties when previous is null and current is not null`() {
        val previous = null
        val current = ObjectsMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.Id, "null", "1"),
            PropertyUpdate(ObjectsMother.Properties.Name, "null", "John"),
            PropertyUpdate(ObjectsMother.Properties.Age, "null", "40")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun `should return list of changes on two non nested objects`() {
        val previous = ObjectsMother.personJohn()
        val current = ObjectsMother.personJohnOlder()

        val diff = DiffTool.diff(previous, current)

        val newAgeProperty = PropertyUpdate(ObjectsMother.Properties.Age, "40", "50")
        assertThat(diff, containsInAnyOrder(newAgeProperty))
    }

    @Test
    fun `should return empty when two objects with nested properties have the same values`() {
        val previous = ObjectsMother.nestedPersonMike()
        val current = ObjectsMother.nestedPersonMike()

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun `should return list of changes with dot notation on two nested objects on a single level`() {
        val previous = ObjectsMother.nestedPersonMike()
        val current = ObjectsMother.nestedPersonMikeMoved()

        val diff = DiffTool.diff(previous, current)

        val newAddressProperties = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.AddressStreet, "10th Street", "Evergreen Avenue"),
            PropertyUpdate(ObjectsMother.Properties.AddressNumber, "412", "1681")
        )
        assertThat(diff, containsInAnyOrder(*newAddressProperties))
    }

    @Test
    fun `should return list of changes with dot notation on two nested objects on a double level`() {
        val previous = ObjectsMother.doubleNestedPersonGary()
        val current = ObjectsMother.doubleNestedPersonGaryOlderMoved()

        val diff = DiffTool.diff(previous, current)

        val newAddressProperties = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.Age, "15", "20"),
            PropertyUpdate(ObjectsMother.Properties.AddressStreet, "Oak Street", "51st Street"),
            PropertyUpdate(ObjectsMother.Properties.AddressNumber, "512", "3610"),
            PropertyUpdate(ObjectsMother.Properties.CityName, "Blue City", "Red City"),
            PropertyUpdate(ObjectsMother.Properties.CityState, "Blue State", "Red State")
        )
        assertThat(diff, containsInAnyOrder(*newAddressProperties))
    }


}