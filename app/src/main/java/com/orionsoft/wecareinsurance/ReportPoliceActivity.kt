package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ReportPoliceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_police)

        val buttonClick = findViewById<Button>(R.id.btn_policy_information)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportDriverDetailsActivity::class.java)
            startActivity(intent)
        }

    }
}