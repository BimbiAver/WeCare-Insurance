package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ReportCaseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_details)

        val buttonClick = findViewById<Button>(R.id.btn_case_details)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportImagesActivity::class.java)
            startActivity(intent)
        }

    }
}