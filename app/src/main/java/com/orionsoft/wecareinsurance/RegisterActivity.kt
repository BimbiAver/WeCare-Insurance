package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val buttonClick = findViewById<Button>(R.id.btn_register1)
        buttonClick.setOnClickListener{
            val intent = Intent(this, RegistersecondActivity::class.java)
            startActivity(intent)
        }

    }
}