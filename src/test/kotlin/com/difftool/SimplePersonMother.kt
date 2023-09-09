package com.difftool

object SimplePersonMother {

    object Properties {
        const val Id = "id"
        const val Name = "name"
        const val Age = "age"
    }

    fun personJohn(): SimplePerson = SimplePerson(1, "John", 40)

    fun personJohnOlder(): SimplePerson = SimplePerson(1, "John", 50)

}