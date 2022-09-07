package com.example.arivali_excel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN

@Suppress("DEPRECATION")
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val i = Intent(
                this@Splash,
                MainActivity::class.java
            )

            startActivity(i)


            finish()
        }, 2000)
    }
}