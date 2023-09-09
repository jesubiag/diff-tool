package com.difftool

import kotlin.reflect.full.memberProperties

object DiffTool {

    const val NULL_VALUE: String = "null"

    inline fun <reified T : Any?> diff(previous: T, current: T): List<ChangeType> = when {
        bothObjectsAreNull(previous, current) -> emptyList()
        bothObjectsAreTheSame(previous, current) -> emptyList()
        onlyCurrentIsNull(previous, current) -> generateChangesListForNonNullObject(previous!!, isPreviousNonNull = true)
        onlyPreviousIsNull(previous, current) -> generateChangesListForNonNullObject(current!!, isPreviousNonNull = false)
        bothHaveSameValues(previous!!, current!!) -> emptyList()
        else -> generateChangesListTwoNonNullObjects(previous, current)
    }

    // Predicates

    fun <T> bothObjectsAreNull(previous: T, current: T): Boolean = previous == null && current == null

    fun <T> bothObjectsAreTheSame(previous: T, current: T): Boolean = previous == current

    fun <T> onlyCurrentIsNull(previous: T, current: T): Boolean = previous != null && current == null

    fun <T> onlyPreviousIsNull(previous: T, current: T): Boolean = previous == null && current != null

    inline fun <reified T : Any> bothHaveSameValues(previous: T, current: T): Boolean =
        T::class.memberProperties
            .map { property -> property.call(previous) to property.call(current) }
            .all { (p1, p2) -> p1 == p2 }

    // Generators

    inline fun <reified T : Any> generateChangesListForNonNullObject(nonNullObject: T, isPreviousNonNull: Boolean): List<ChangeType> =
        T::class.memberProperties.map { property ->
            val propertyStringValue = property.call(nonNullObject).toString()
            if (isPreviousNonNull)
                PropertyUpdate(property.name, propertyStringValue, NULL_VALUE)
            else
                PropertyUpdate(property.name, NULL_VALUE, propertyStringValue)
        }

    inline fun <reified T : Any> generateChangesListTwoNonNullObjects(previous: T, current: T): List<ChangeType> {
        return T::class.memberProperties
            .map { property -> PropertyUpdate(property.name, property.call(previous), property.call(current))
            }
            .filter { (_, previousProperty, currentProperty) -> previousProperty != currentProperty }
    }

}