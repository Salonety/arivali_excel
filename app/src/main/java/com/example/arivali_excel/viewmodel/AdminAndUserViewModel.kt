package com.example.arivali_excel.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arivali_excel.database.Student
import com.example.arivali_excel.database.UserDatabase
import com.example.arivali_excel.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminAndUserViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

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

    fun searchDatabase(searchQuery: String): LiveData<List<Student>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

}