package com.difftool

sealed interface ChangeType {
    val property: String
    val previous: String
    val current: String
}

data class PropertyUpdate(override val property: String,
                          override val previous: String,
                          override val current: String) : ChangeType {}

data class ListUpdate(override val property: String,
                      override val previous: String,
                      override val current: String) : ChangeType {}