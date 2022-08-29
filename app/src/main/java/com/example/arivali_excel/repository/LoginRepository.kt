package com.example.arivali_excel.repository

import androidx.lifecycle.LiveData
import com.example.arivali_excel.database.User
import com.example.arivali_excel.database.UserDao

class LoginRepository(private val userDao: UserDao) {

    fun getUser(email: String, password: String): LiveData<User> = userDao.getUser(email, password)

    val allUser: LiveData<List<User>> = userDao.getAllUser()

    suspend fun insert(users: MutableList<User>) {
        userDao.insertUser(users)
    }
}