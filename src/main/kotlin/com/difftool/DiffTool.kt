package com.difftool

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

typealias Properties<T> = Collection<KProperty1<out T, *>>

object DiffTool {

    private const val NULL_VALUE: String = "null"

    inline fun <reified T : Any> diff(previous: T?, current: T?): List<ChangeType> =
        detectAndBuildChanges(previous, current, T::class.memberProperties)

    fun <T> detectAndBuildChanges(previous: T?, current: T?, properties: Properties<T>, suffix: String = ""): List<ChangeType> = when {
        bothObjectsAreNull(previous, current) -> emptyList()

        bothObjectsAreTheSame(previous, current) -> emptyList()

        onlyCurrentIsNull(previous, current) -> generateChangesListForNonNullObject(previous!!, isPreviousNonNull = true, properties)

        onlyPreviousIsNull(previous, current) -> generateChangesListForNonNullObject(current!!, isPreviousNonNull = false, properties)

        bothHaveSameValues(previous!!, current!!, properties) -> emptyList()

        else -> generateChangesListTwoNonNullObjects(previous, current, properties, suffix)
    }

    // Predicates

    private fun <T> bothObjectsAreNull(previous: T, current: T): Boolean = previous == null && current == null

    private fun <T> bothObjectsAreTheSame(previous: T, current: T): Boolean = previous == current

    private fun <T> onlyCurrentIsNull(previous: T, current: T): Boolean = previous != null && current == null

    private fun <T> onlyPreviousIsNull(previous: T, current: T): Boolean = previous == null && current != null

    private fun <T> bothHaveSameValues(previous: T, current: T, properties: Properties<T>): Boolean =
        properties.map { property -> property.call(previous) to property.call(current) }
            .all { (p1, p2) -> p1 == p2 }

    // Generators

    private fun <T> generateChangesListForNonNullObject(nonNullObject: T,
                                                        isPreviousNonNull: Boolean,
                                                        properties: Properties<T>): List<ChangeType> =
        properties.map { property ->
            val propertyStringValue = property.call(nonNullObject).toString()
            if (isPreviousNonNull)
                PropertyUpdate(property.name, propertyStringValue, NULL_VALUE)
            else
                PropertyUpdate(property.name, NULL_VALUE, propertyStringValue)
        }

    private fun <T> generateChangesListTwoNonNullObjects(previous: T,
                                                         current: T,
                                                         properties: Properties<T>,
                                                         suffix: String = ""): List<ChangeType> =
        properties.flatMap { property ->
            val propertyName = property.name
            if (isTerminal(property.returnType))
                listOf(PropertyUpdate("$suffix$propertyName", property.call(previous), property.call(current)))
            else {
                val subProperties = (property.returnType.classifier as? KClass<*>)?.memberProperties ?: throw Exception("Unable to get subproperties for $propertyName")
                detectAndBuildChanges(property.call(previous), property.call(current), subProperties, "$suffix$propertyName.")
            }
        }.filter { changeType -> changeType.previous != changeType.current }

    // Other

    private fun isTerminal(kType: KType): Boolean = kType.isSubtypeOf(typeOf<Int>())
            || kType.isSubtypeOf(typeOf<String>())
            || kType.isSubtypeOf(typeOf<Boolean>())
            || kType.isSubtypeOf(typeOf<Long>())
            || kType.isSubtypeOf(typeOf<Double>())
            || kType.isSubtypeOf(typeOf<Float>())
            || kType.isSubtypeOf(typeOf<Char>())
            || kType.isSubtypeOf(typeOf<Number>())

}