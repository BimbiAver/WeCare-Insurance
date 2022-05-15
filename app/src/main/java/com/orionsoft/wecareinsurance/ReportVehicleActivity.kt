package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ReportVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_vehicle)

        val buttonClick = findViewById<Button>(R.id.btn_next_vehicle_details)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportPoliceActivity::class.java)
            startActivity(intent)
        }


        val buttonClick2 = findViewById<ImageButton>(R.id.img_btn_back_vehicle)
        buttonClick2.setOnClickListener{
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }
}