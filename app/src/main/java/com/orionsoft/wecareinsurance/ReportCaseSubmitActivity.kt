package com.orionsoft.wecareinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.orionsoft.wecareinsurance.model.Case

class ReportCaseSubmitActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnRCSSuccess: Button

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRCSSuccess -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finishAffinity()
                finish()
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_submit)

        btnRCSSuccess = findViewById(R.id.btnRCSSuccess)

        // Instantiate the setOnClickListener(s) at runtime
        btnRCSSuccess.setOnClickListener(this)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finishAffinity()
        finish()
    }
}