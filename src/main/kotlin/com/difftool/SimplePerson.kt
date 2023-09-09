package com.difftool

class SimplePerson(val id: Long, val name: String, val age: Int)

class SingleNestedPerson(val id: Long, val name: String, val age: Int, val address: Address)

class Address(val street: String, val number: Int)

class DoubleNestedPerson(val id: Long, val name: String, val age: Int, val address: NestedAddress)

class NestedAddress(val street: String, val number: Int, val city: City)

class City(val name: String, val state: String)