package com.difftool

import kotlin.reflect.full.memberProperties

object DiffTool {

    const val NULL_VALUE: String = "null"

    inline fun <reified T : Any?> diff(previous: T, current: T): List<ChangeType> = when {
        bothObjectsAreNull(previous, current) -> emptyList()
        bothObjectsAreTheSame(previous, current) -> emptyList()
        onlyCurrentIsNull(previous, current) -> generateChangesListForNonNullObject(previous!!, isPreviousNonNull = true)
        onlyPreviousIsNull(previous, current) -> generateChangesListForNonNullObject(current!!, isPreviousNonNull = false)
        else -> TODO()
    }

    fun <T> bothObjectsAreNull(previous: T, current: T): Boolean = previous == null && current == null

    fun <T> bothObjectsAreTheSame(previous: T, current: T): Boolean = previous == current

    fun <T> onlyCurrentIsNull(previous: T, current: T): Boolean = previous != null && current == null

    fun <T> onlyPreviousIsNull(previous: T, current: T): Boolean = previous == null && current != null

    inline fun <reified T : Any> generateChangesListForNonNullObject(nonNullObject: T, isPreviousNonNull: Boolean): List<ChangeType> =
        T::class.memberProperties.map { member ->
            val propertyStringValue = member.call(nonNullObject).toString()
            if (isPreviousNonNull)
                PropertyUpdate(member.name, propertyStringValue, NULL_VALUE)
            else
                PropertyUpdate(member.name, NULL_VALUE, propertyStringValue)
        }

}