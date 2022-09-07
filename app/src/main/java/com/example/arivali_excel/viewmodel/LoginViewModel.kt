package com.example.arivali_excel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.arivali_excel.database.User
import com.example.arivali_excel.database.UserDatabase
import com.example.arivali_excel.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private var allUser: LiveData<List<User>>
    private val repository: LoginRepository

    init {
        val dao = UserDatabase.getDatabase(application).userDao()
        repository = LoginRepository(dao)
        allUser = repository.allUser
    }

    fun insertUsers(user: MutableList<User>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun getUser(email: String, password: String): LiveData<User> =
        repository.getUser(email, password)


}