package com.difftool

object ObjectsMother {

    object Properties {
        // SimplePerson
        const val Id = "id"
        const val Name = "name"
        const val Age = "age"

        // Address (nested)
        const val AddressStreet = "address.street"
        const val AddressNumber = "address.number"

        // City (nested)
        const val CityName = "address.city.name"
        const val CityState = "address.city.state"
    }

    fun personJohn(): SimplePerson = SimplePerson(1, "John", 40)

    fun personJohnOlder(): SimplePerson = SimplePerson(1, "John", 50)

    fun nestedPersonMike(): SingleNestedPerson = SingleNestedPerson(2, "Mike", 30, anAddress())

    fun nestedPersonMikeMoved(): SingleNestedPerson = SingleNestedPerson(2, "Mike", 30, aDifferentAddress())

    fun doubleNestedPersonGary(): DoubleNestedPerson = DoubleNestedPerson(3, "Gary", 15, aNestedAddress())

    fun doubleNestedPersonGaryOlderMoved(): DoubleNestedPerson = DoubleNestedPerson(3, "Gary", 20, aDifferentNestedAddress())

    private fun anAddress(): Address = Address("10th Street", 412)

    private fun aDifferentAddress(): Address = Address("Evergreen Avenue", 1681)

    private fun aNestedAddress(): NestedAddress = NestedAddress("Oak Street", 512, aCity())

    private fun aDifferentNestedAddress(): NestedAddress = NestedAddress("51st Street", 3610, aDifferentCity())

    private fun aCity(): City = City("Blue City", "Blue State")

    private fun aDifferentCity(): City = City("Red City", "Red State")

}