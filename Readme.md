# Diff Tool

### How it works
This tool works basically by using reflection.

First of all, it performs different checks to address different edge cases, as objects being the same instance or being both null. If none of these edge cases are met, then it procedes to make a full comparison of all properties. On this stage, it traverses the different properties for the object type, and checks if the property is a terminal or primitive type, a collection or array, or some other type as an object with it's subproperties.

Depending on the case, it performs different logics, but basically it tries to get the corresponding value for the property or recursively keep iterating until it finds all corresponding values and identifies the differences, according to the defined rules.

When all properties have been compared, the collected differences are gathered up and then returned.

Different tests were created along the development to add new features and check all code was meeting the requirements.

### Total time
This took me about 7 hours.