package com.difftool

class SimpleStudent(val id: Int, val name: String, val subjects: List<String>)

class StudentWithoutIdFieldOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithoutIdField>)

class SubjectWithoutIdField(val name: String)

class StudentWithIdFieldOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithIdField>)

data class SubjectWithIdField(val id: String, val name: String) {
    override fun toString(): String {
        return name
    }
}

class StudentWithAnnotatedIdOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithAnnotatedId>)

class SubjectWithAnnotatedId(@AuditKey val name: String)