package com.difftool

object SimplePersonMother {

    object Properties {
        const val Id = "id"
        const val Name = "name"
        const val Age = "age"
    }

    fun aPerson(): SimplePerson = SimplePerson(1, "John", 40)

}