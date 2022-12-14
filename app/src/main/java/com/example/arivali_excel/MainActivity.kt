package com.example.arivali_excel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.arivali_excel.Util.Constant.SHARED_PREF
import com.example.arivali_excel.Util.Constant.USER_TYPE
import com.example.arivali_excel.database.User
import com.example.arivali_excel.databinding.ActivityMainBinding
import com.example.arivali_excel.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var check: Boolean = true
    private var userList: MutableList<User>? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LoginViewModel::class.java]

        sharedPreferences = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

        userList = mutableListOf()
        userList?.add(User(0, "saloni11@gmail.com", "1234", "admin"))

        userList?.add(User(0, "saloni12@gmail.com", "12345", "user"))

        check = sharedPreferences.getBoolean("check_key", true)



        if (check) {
            insertUser(userList!!)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("check_key", false)
            editor.apply()
            editor.commit()
        }

        binding.btnLogin.setOnClickListener {

            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etEmail.error = "Enter Email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Enter Password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            } else {
                viewModel.getUser(email, password).observe(this) {
                    if (it != null) {
                        val intent = Intent(this, AdminAndUserActivity::class.java)
                        intent.putExtra(USER_TYPE, it.userType)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Email and password are not matching",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }



    }


    private fun insertUser(user: MutableList<User>) {
        viewModel.insertUsers(user)
    }

}