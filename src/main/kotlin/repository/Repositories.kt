package repository

import repository.impl.Group
import repository.impl.Student


interface  StudentRepository{
    fun updateStudent(id: Int, newName: String)
    fun addNewStudent(name: String,  grId: Int): Student?
    fun selectByID(id: Int): Student?
    fun selectAll(): List<Student?>
    fun deleteById(id: Int)
    fun addPerson()
}

interface GroupRepository{
    fun studentsOfTheGroup(grId: Int): List<Student?>
    fun selectByID(id: Int): Group?
    fun selectAll(): List<Group?>
    fun addNewGroup(name: String)
    fun deleteById(id: Int)
}

