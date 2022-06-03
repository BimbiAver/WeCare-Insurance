package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ReportCaseDriverDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_driver_details)

        val buttonClick = findViewById<Button>(R.id.btn_details_driver)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportCaseDetailsActivity::class.java)
            startActivity(intent)
        }

    }
}