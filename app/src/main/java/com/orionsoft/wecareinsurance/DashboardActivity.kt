package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val buttonClick = findViewById<ImageButton>(R.id.img_btn_report_case)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportVehicleActivity::class.java)
            startActivity(intent)
        }

        val buttonClick2 = findViewById<ImageButton>(R.id.img_btn_previous)
        buttonClick2.setOnClickListener{
            val intent = Intent(this, PreviousCasesActivity::class.java)
            startActivity(intent)
        }
        val buttonClick3 = findViewById<ImageButton>(R.id.img_btn_vehicle)
        buttonClick3.setOnClickListener{
            val intent = Intent(this, RegisteredVehiclesActivity::class.java)
            startActivity(intent)
        }
        val buttonClick4 = findViewById<ImageButton>(R.id.img_btn_contact)
        buttonClick4.setOnClickListener{
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

    }
}