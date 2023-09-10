package com.difftool

object ObjectsMother {

    object Properties {
        // SimplePerson
        const val PersonId = "id"
        const val PersonName = "name"
        const val PersonAge = "age"

        // Address (nested)
        const val AddressStreet = "address.street"
        const val AddressNumber = "address.number"

        // City (nested)
        const val CityName = "address.city.name"
        const val CityState = "address.city.state"

        // SimpleStudent
        const val StudentId = "id"
        const val StudentName = "name"
        const val StudentSubjects = "subjects"
    }

    fun personJohn(): SimplePerson = SimplePerson(1, "John", 40)

    fun personJohnOlder(): SimplePerson = SimplePerson(1, "John", 50)

    fun nestedPersonMike(): SingleNestedPerson = SingleNestedPerson(2, "Mike", 30, anAddress())

    fun nestedPersonMikeMoved(): SingleNestedPerson = SingleNestedPerson(2, "Mike", 30, aDifferentAddress())

    fun doubleNestedPersonGary(): DoubleNestedPerson = DoubleNestedPerson(3, "Gary", 15, aNestedAddress())

    fun doubleNestedPersonGaryOlderMoved(): DoubleNestedPerson = DoubleNestedPerson(3, "Gary", 20, aDifferentNestedAddress())

    fun studentBrian(): SimpleStudent = SimpleStudent(100, "Brian", subjectsWithAnnotatedId())

    fun studentBrianOtherSubjects(): SimpleStudent = SimpleStudent(100, "Brian", otherSubjectsWithAnnotatedId())

    fun studentDavidWithoutIdsOnList(): StudentWithoutIdFieldOnListElement = StudentWithoutIdFieldOnListElement(3333, "David", subjectsWithoutIdField())

    fun studentDavidWithoutIdsOnListAndOtherSubjects(): StudentWithoutIdFieldOnListElement = StudentWithoutIdFieldOnListElement(3333, "David", otherSubjectsWithoutIdField())

    fun studentLisaWithIdFieldOnList(): StudentWithIdFieldOnListElement = StudentWithIdFieldOnListElement(987, "Lisa", subjectsWithIdField())

    fun studentLisaWithIdFieldOnListAndOtherSubjects(): StudentWithIdFieldOnListElement = StudentWithIdFieldOnListElement(987, "Lisa", otherSubjectsWithIdField())

    fun studentAnneWithAnnotatedIdOnList(): StudentWithAnnotatedIdOnListElement = StudentWithAnnotatedIdOnListElement(1908, "Anne", subjectsWithAnnotatedId())

    fun studentAnneWithAnnotatedIdOnListAndOtherSubjects(): StudentWithAnnotatedIdOnListElement = StudentWithAnnotatedIdOnListElement(1908, "Anne", otherSubjectsWithAnnotatedId())

    private fun anAddress(): Address = Address("10th Street", 412)

    private fun aDifferentAddress(): Address = Address("Evergreen Avenue", 1681)

    private fun aNestedAddress(): NestedAddress = NestedAddress("Oak Street", 512, aCity())

    private fun aDifferentNestedAddress(): NestedAddress = NestedAddress("51st Street", 3610, aDifferentCity())

    private fun aCity(): City = City("Blue City", "Blue State")

    private fun aDifferentCity(): City = City("Red City", "Red State")

    private fun subjectsWithoutIdField() = listOf(SubjectWithoutIdField("Math"), SubjectWithoutIdField("English"))

    private fun otherSubjectsWithoutIdField() = listOf(SubjectWithoutIdField("Math"), SubjectWithoutIdField("English"))

    private fun subjectsWithIdField() = listOf(SubjectWithIdField("001_math", "Math"), SubjectWithIdField("062_English", "English"))

    private fun otherSubjectsWithIdField() = listOf(SubjectWithIdField("999_math", "Math"), SubjectWithIdField("555_English", "English"))

    private fun subjectsWithAnnotatedId() = listOf(SubjectWithAnnotatedId("Math"), SubjectWithAnnotatedId("English"))

    private fun otherSubjectsWithAnnotatedId() = listOf(SubjectWithAnnotatedId("English"), SubjectWithAnnotatedId("Biology"))

}