package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ReportCaseImagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_images)

        val buttonClick = findViewById<Button>(R.id.btn_case_images)
        buttonClick.setOnClickListener{
            val intent = Intent(this, ReportCaseSubmitActivity::class.java)
            startActivity(intent)
        }

    }
}