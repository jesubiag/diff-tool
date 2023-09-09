package com.difftool

sealed interface ChangeType {
    val property: String
    val hasChanged: Boolean
}

data class PropertyUpdate(override val property: String,
                          val previous: String,
                          val current: String) : ChangeType {

    override val hasChanged: Boolean = previous != current

    constructor(property: String, previous: Any?, current: Any?)
            : this(property, previous?.toString() ?: "null", current?.toString() ?: "null")

}

data class ListUpdate(override val property: String,
                      val added: List<String>,
                      val removed: List<String>) : ChangeType {

    override val hasChanged: Boolean = added.isNotEmpty() || removed.isNotEmpty()

}