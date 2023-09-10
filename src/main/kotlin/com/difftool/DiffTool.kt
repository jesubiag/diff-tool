package com.difftool

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.typeOf

typealias Properties<T> = Collection<KProperty1<out T, *>>

object DiffTool {

    private const val NULL_VALUE: String = "null"

    inline fun <reified T : Any> diff(previous: T?, current: T?): List<ChangeType> =
        detectAndBuildChanges(previous, current, T::class.memberProperties)

    fun <T> detectAndBuildChanges(previous: T?,
                                  current: T?,
                                  properties: Properties<T>,
                                  suffix: String = ""): List<ChangeType> = when {
        bothObjectsAreNull(previous, current) -> emptyList()
        bothObjectsAreTheSame(previous, current) -> emptyList()
        onlyCurrentIsNull(previous, current) -> generateChangesListForNonNullObject(previous!!, isPreviousNonNull = true, properties)
        onlyPreviousIsNull(previous, current) -> generateChangesListForNonNullObject(current!!, isPreviousNonNull = false, properties)
        bothHaveSameValues(previous!!, current!!, properties) -> emptyList()
        else -> generateChangesListForTwoNonNullObjects(previous, current, properties, suffix)
    }

    // Predicates

    private fun <T> bothObjectsAreNull(previous: T, current: T): Boolean = previous == null && current == null

    private fun <T> bothObjectsAreTheSame(previous: T, current: T): Boolean = previous == current

    private fun <T> onlyCurrentIsNull(previous: T, current: T): Boolean = previous != null && current == null

    private fun <T> onlyPreviousIsNull(previous: T, current: T): Boolean = previous == null && current != null

    private fun <T> bothHaveSameValues(previous: T, current: T, properties: Properties<T>): Boolean =
        properties
            .map { property -> property.call(previous) to property.call(current) }
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

    private fun <T> generateChangesListForTwoNonNullObjects(previous: T,
                                                            current: T,
                                                            properties: Properties<T>,
                                                            suffix: String = ""): List<ChangeType> =
        properties.flatMap { property ->
            val propertyName = property.name
            val propertyType = property.returnType
            val fullPropertyName = "$suffix$propertyName"
            when {
                isTerminal(propertyType) -> {
                    listOf(PropertyUpdate(fullPropertyName, property.call(previous), property.call(current)))
                }
                isCollectionOrArray(propertyType) -> {
                    val collectionInnerType = getCollectionInnerType(propertyType)

                    if (!isTerminal(collectionInnerType)) {
                        validateId(collectionInnerType)
                    }

                    // TODO: check if collection inner type is terminal

                    val previousCollection = toCollection(propertyType, property.call(previous))
                    val currentCollection = toCollection(propertyType, property.call(current))
                    val removed = previousCollection.minus(currentCollection.toSet()).map(Any?::toString)
                    val added = currentCollection.minus(previousCollection.toSet()).map(Any?::toString)
                    listOf(ListUpdate(fullPropertyName, added, removed))
                }
                else -> {
                    val subProperties = subProperties(property.returnType)
                    detectAndBuildChanges(property.call(previous), property.call(current), subProperties, "$fullPropertyName.")
                }
            }
        }.filter { changeType -> changeType.hasChanged }

    // Other

    private fun isTerminal(kType: KType): Boolean = kType.isSubtypeOf(typeOf<Int>())
            || kType.isSubtypeOf(typeOf<String>())
            || kType.isSubtypeOf(typeOf<Boolean>())
            || kType.isSubtypeOf(typeOf<Long>())
            || kType.isSubtypeOf(typeOf<Double>())
            || kType.isSubtypeOf(typeOf<Float>())
            || kType.isSubtypeOf(typeOf<Char>())
            || kType.isSubtypeOf(typeOf<Number>())

    private fun isCollectionOrArray(kType: KType): Boolean = isCollection(kType) || isArray(kType)

    private fun isArray(kType: KType): Boolean = kType.isSubtypeOf(Array<Any>::class.starProjectedType)

    private fun isCollection(kType: KType): Boolean = kType.isSubtypeOf(Collection::class.starProjectedType)

    private fun toCollection(propertyType: KType, collectionOrArray: Any?): Collection<*> =
        if (isCollection(propertyType)) {
            collectionOrArray as Collection<*>
        } else {
            val destination = mutableSetOf<Any?>()
            (collectionOrArray as Array<*>).toCollection(destination)
            destination
        }

    private fun subProperties(kType: KType): Collection<KProperty1<out Any, *>> =
        (kType.classifier as KClass<*>).memberProperties

    private fun validateId(collectionInnerType: KType?) {
        if (collectionInnerType == null || !hasId(collectionInnerType))
            throw TypeWithoutIdException(collectionInnerType?.toString() ?: "UnknownType")
    }

    private fun getCollectionInnerType(kType: KType): KType {
        var currentType = kType

        while (true) {
            currentType = if (isCollection(currentType)) {
                val elementType = currentType.arguments.firstOrNull()?.type ?: TODO() // TODO
                elementType
            } else if (isArray(currentType)) {
                currentType.arguments.firstOrNull()?.type ?: TODO() // TODO
            } else {
                return currentType
            }
        }
    }

    private fun hasId(kType: KType): Boolean =
        subProperties(kType)
            .any { subProperty -> subProperty.name == "id"
                    || subProperty.annotations.any { annotation -> annotation is AuditKey }
            }
}