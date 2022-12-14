package com.example.arivali_excel.viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.arivali_excel.database.Student
import com.example.arivali_excel.database.UserDatabase
import com.example.arivali_excel.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminAndUserViewModel  constructor(application: Application): AndroidViewModel(application) {

    var allStudent : LiveData<List<Student>>
    private val repository: StudentRepository

    init {
        val dao = UserDatabase.getDatabase(application).studentDao()
        repository = StudentRepository(dao)
        allStudent = repository.allStudent
    }

    fun insertStudent(student:MutableList<Student>) = viewModelScope.launch(Dispatchers.IO){
        repository.insertStudent(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.upDateStudent(student)
    }
    fun searchForItems(name: String) : LiveData<List<Student>> {
        return repository.search(name)
    }


}