package com.difftool

class TypeWithoutIdException(typeName: String) : Exception("Type $typeName has no field defined as an id")