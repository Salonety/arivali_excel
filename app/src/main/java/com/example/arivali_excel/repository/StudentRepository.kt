package com.example.arivali_excel.repository


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.arivali_excel.database.Student
import com.example.arivali_excel.database.StudentDao
import javax.inject.Inject

class StudentRepository @Inject constructor(private val studentDao: StudentDao) {


    val allStudent: LiveData<List<Student>> = studentDao.getAllStudent()


    suspend fun insertStudent(student: MutableList<Student>) {
        studentDao.insertStudent(student)
    }

    suspend fun upDateStudent(student: Student) {
        studentDao.updateStudent(student)

    }
    @WorkerThread
    fun search(name : String) : LiveData<List<Student>>{
        return studentDao.getSearchResults(name)
    }

}