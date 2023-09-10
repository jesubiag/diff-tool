package com.difftool

import com.sun.org.slf4j.internal.LoggerFactory

class SimpleStudent(val id: Int, val name: String, val subjects: List<SubjectWithAnnotatedId>) {
    override fun toString(): String = "id=$id, name=$name, subjects=$subjects"
}

class StudentWithoutIdFieldOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithoutIdField>)

class SubjectWithoutIdField(val name: String)

class StudentWithIdFieldOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithIdField>)

class SubjectWithIdField(val id: String, val name: String)

class StudentWithAnnotatedIdOnListElement(val id: Int, val name: String, val subjects: List<SubjectWithAnnotatedId>)

// TODO: temporal fix to make tests pass, remove "data"
data class SubjectWithAnnotatedId(@AuditKey val name: String) {
    override fun toString(): String = name
}