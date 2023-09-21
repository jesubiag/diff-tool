package com.difftool

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DiffToolTest {

    @Test
    fun shouldReturnNoChangesWhenPreviousAndCurrentObjectsAreNull() {
        val previous: SimplePerson? = null
        val current: SimplePerson? = null

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun shouldReturnEmptyWhenBothObjectsAreTheSame() {
        val previous = ObjectsMother.personJohn()
        val current = previous

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun shouldReturnEmptyWhenCurrentObjectDidNotChange() {
        val previous = ObjectsMother.personJohn()
        val current = ObjectsMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun shouldReturnNullPropertiesWhenPreviousObjectIsFullyDefinedAndCurrentObjectIsNull() {
        val previous = ObjectsMother.personJohn()
        val current = null

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.PersonId, "1", "null"),
            PropertyUpdate(ObjectsMother.Properties.PersonName, "John", "null"),
            PropertyUpdate(ObjectsMother.Properties.PersonAge, "40", "null")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun shouldReturnAllNotNullObjectPropertiesWhenPreviousIsNullAndCurrentIsNotNull() {
        val previous = null
        val current = ObjectsMother.personJohn()

        val diff = DiffTool.diff(previous, current)

        val allCurrentPropertiesNull = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.PersonId, "null", "1"),
            PropertyUpdate(ObjectsMother.Properties.PersonName, "null", "John"),
            PropertyUpdate(ObjectsMother.Properties.PersonAge, "null", "40")
        )
        assertThat(diff, containsInAnyOrder(*allCurrentPropertiesNull))
    }

    @Test
    fun shouldReturnSinglePropertyUpdateWhenJustChangingRootPrimitiveProperty() {
        val previous = ObjectsMother.personJohn()
        val current = ObjectsMother.personJohnOlder()

        val diff = DiffTool.diff(previous, current)

        val newAgeProperty = PropertyUpdate(ObjectsMother.Properties.PersonAge, "40", "50")
        assertThat(diff, containsInAnyOrder(newAgeProperty))
    }

    @Test
    fun shouldReturnNoChangesWhenTwoObjectsWithNestedPropertiesAreTheSame() {
        val previous = ObjectsMother.nestedPersonMike()
        val current = ObjectsMother.nestedPersonMike()

        val diff = DiffTool.diff(previous, current)

        assertThat(diff, empty())
    }

    @Test
    fun shouldReturnListOfChangesWithDotNotationOnTwoNestedObjectsOnASingleLevel() {
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
    fun shouldReturnListOfChangesWithDotNotationOnTwoNestedObjectsOnADoubleLevel() {
        val previous = ObjectsMother.doubleNestedPersonGary()
        val current = ObjectsMother.doubleNestedPersonGaryOlderMoved()

        val diff = DiffTool.diff(previous, current)

        val newAddressProperties = arrayOf(
            PropertyUpdate(ObjectsMother.Properties.PersonAge, "15", "20"),
            PropertyUpdate(ObjectsMother.Properties.AddressStreet, "Oak Street", "51st Street"),
            PropertyUpdate(ObjectsMother.Properties.AddressNumber, "512", "3610"),
            PropertyUpdate(ObjectsMother.Properties.CityName, "Blue City", "Red City"),
            PropertyUpdate(ObjectsMother.Properties.CityState, "Blue State", "Red State")
        )
        assertThat(diff, containsInAnyOrder(*newAddressProperties))
    }

    @Test
    fun shouldReturnListOfChangesWithAddedAndRemovedWhenPropertyIsAList() {
        val previous = ObjectsMother.studentBrian()
        val current = ObjectsMother.studentBrianOtherSubjects()

        val diff = DiffTool.diff(previous, current)

        val changedSubjectsProperties = arrayOf(
            ListUpdate(ObjectsMother.Properties.StudentSubjects, listOf("Algebra", "Art"), listOf("English"))
        )
        assertThat(diff, containsInAnyOrder(*changedSubjectsProperties))
    }

    @Test
    fun shouldFailWithTypeWithoutIdExceptionWhenListItemHasNoIdFieldDefined() {
        val previous = ObjectsMother.studentDavidWithoutIdsOnList()
        val current = ObjectsMother.studentDavidWithoutIdsOnListAndOtherSubjects()

        val diff: () -> Unit = { DiffTool.diff(previous, current) }

        assertThrows<TypeWithoutIdException>(diff)
    }

    @Test
    fun shouldNotFailWhenListItemHasIdFieldDefined() {
        val previous = ObjectsMother.studentLisaWithIdFieldOnList()
        val current = ObjectsMother.studentLisaWithIdFieldOnListAndOtherSubjects()

        val diff: () -> Unit = { DiffTool.diff(previous, current) }

        assertDoesNotThrow(diff)
    }

    @Test
    fun shouldNotFailWhenListItemHasAnnotatedId() {
        val previous = ObjectsMother.studentAnneWithAnnotatedIdOnList()
        val current = ObjectsMother.studentAnneWithAnnotatedIdOnListAndOtherSubjects()

        val diff: () -> Unit = { DiffTool.diff(previous, current) }

        assertDoesNotThrow(diff)
    }

    @Test
    fun shouldDisplayChangedElementInListWithIdInSquareBracketsAndAddedAndRemovedElements() {
        val previous = ObjectsMother.studentLisaWithIdFieldOnList()
        val current = ObjectsMother.studentLisaWithIdFieldOnListAndOtherSubjectsWithDifferentName()

        val diff = DiffTool.diff(previous, current)

        val changedSubjectsProperties = arrayOf(
            ListUpdate(ObjectsMother.Properties.StudentSubjects, listOf("Art"), listOf("English")),
            PropertyUpdate("subjects[001_math].name", "Math", "Math I")
        )
        assertThat(diff, containsInAnyOrder(*changedSubjectsProperties))
    }

    @Test
    fun shouldIdentifyAllChangesInAMoreComplexScenarioWithMultipleNestedProperties() {
        val countryName = "Country name"
        val country = Country(countryName)
        val artist = Artist("Artist Name", country)
        val songs = listOf(
            Song(SongId("sn1"), "Song name 1", 1, artist),
            Song(SongId("sn2"), "Song name 2", 2, artist),
            Song(SongId("sn3"), "Song name 3", 3, artist),
            Song(SongId("sn4"), "Song name 4", 4, artist)
        )
        val genres = listOf("Genre 1", "Genre 2", "Genre 3")
        val previous = Album("Album name", 2008, songs, genres)

        val otherCountryName = "Other Country name"
        val otherCountry = Country(otherCountryName)
        val artistMoved = Artist("Artist Name", otherCountry)
        val differentSongs = listOf(
            Song(SongId("sn1"), "Song name 1", 1, artistMoved),
            Song(SongId("sn2"), "Song name 22", 22, artistMoved),
            Song(SongId("sn10"), "Song name 10", 10, artist)
        )
        val differentGenres = listOf("Genre 1", "Genre 4")
        val current = Album("Album name", 2030, differentSongs, differentGenres)

        val diff = DiffTool.diff(previous, current)

        val changedProperties = arrayOf(
            PropertyUpdate("year", "2008", "2030"),
            PropertyUpdate("songs[sn1].artist.country.name", countryName, otherCountryName),
            PropertyUpdate("songs[sn2].artist.country.name", countryName, otherCountryName),
            PropertyUpdate("songs[sn2].name", "Song name 2", "Song name 22"),
            PropertyUpdate("songs[sn2].number", "2", "22"),
            ListUpdate("songs", listOf("Song name 10"), listOf("Song name 3", "Song name 4")),
            ListUpdate("genres", listOf("Genre 4"), listOf("Genre 2", "Genre 3"))
        )
        assertThat(diff, containsInAnyOrder(*changedProperties))
    }

}