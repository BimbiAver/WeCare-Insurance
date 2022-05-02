package com.example.testinginsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Splash_Screen : AppCompatActivity() {

    private var SPLASH_TIME: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent (this, MainActivity::class.java))
                finish()

        }, SPLASH_TIME)
    }
}