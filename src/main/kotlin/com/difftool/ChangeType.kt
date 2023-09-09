package com.difftool

sealed interface ChangeType {}

class PropertyUpdate : ChangeType {}

class ListUpdate : ChangeType {}