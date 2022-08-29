package com.example.arivali_excel.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val email: String,
    val password: String,
    val userType: String
)