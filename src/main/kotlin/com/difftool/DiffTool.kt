package com.difftool

object DiffTool {

    fun <T> diff(o1: T, o2: T): List<ChangeType> = when {
        bothObjectsAreNull(o1, o2) -> emptyList()
        bothObjectsAreTheSame(o1, o2) -> emptyList()
        else -> TODO()
    }

    private fun <T> bothObjectsAreNull(o1: T, o2: T): Boolean = o1 == null && o2 == null

    private fun <T> bothObjectsAreTheSame(o1: T, o2: T): Boolean = o1 == o2

}