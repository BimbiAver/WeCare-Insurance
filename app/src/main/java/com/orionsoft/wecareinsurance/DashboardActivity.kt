package com.orionsoft.wecareinsurance

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var txtDashboardGreet: TextView
    private lateinit var btnLogOut: Button
    private lateinit var imgBtnProfile: ImageButton
    private lateinit var cdViewReportCase: CardView
    private lateinit var cdViewPrevCases: CardView
    private lateinit var cdViewRegVehicles: CardView
    private lateinit var cdViewContactUs: CardView

    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences for login session

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogOut -> {
                logout() // User Logout
            }
            R.id.cdViewReportCase -> {
                val intent = Intent(this, ReportCaseVehicleActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.cdViewPrevCases -> {
                val intent = Intent(this, PreviousCasesActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.cdViewRegVehicles -> {
                val intent = Intent(this, RegisteredVehiclesActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.cdViewContactUs -> {
                val intent = Intent(this, ContactActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.imgBtnProfile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        txtDashboardGreet = findViewById(R.id.txtDashboardGreet)
        btnLogOut = findViewById(R.id.btnLogOut)
        imgBtnProfile = findViewById(R.id.imgBtnProfile)
        cdViewReportCase = findViewById(R.id.cdViewReportCase)
        cdViewPrevCases = findViewById(R.id.cdViewPrevCases)
        cdViewRegVehicles = findViewById(R.id.cdViewRegVehicles)
        cdViewContactUs = findViewById(R.id.cdViewContactUs)

        var greetName = sharedPrefManager!!.getPreferenceString("name")
        txtDashboardGreet.text = "Hello, " + greetName!!.substring(greetName.lastIndexOf(" ")+1)

        // Instantiate the setOnClickListener(s) at runtime
        btnLogOut.setOnClickListener(this)
        imgBtnProfile.setOnClickListener(this)
        cdViewReportCase.setOnClickListener(this)
        cdViewPrevCases.setOnClickListener(this)
        cdViewRegVehicles.setOnClickListener(this)
        cdViewContactUs.setOnClickListener(this)
    }

//        -----------------------------------------------------------------------------------------------

    // When the BACK button is pressed, the activity on the stack is restarted
    override fun onRestart() {
        super.onRestart()
        finishAffinity()
        finish()
        startActivity(intent)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_exit)
            .setTitle("Confirm Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                finishAffinity() // Removes the connection of the existing activity to its stack
                finish() // The method onDestroy() is executed & exit the application
            })
            .setNegativeButton("No", null).show()
    }

//        -----------------------------------------------------------------------------------------------

    private fun logout() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_exit)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                sharedPrefManager!!.clearSharedPreference()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finishAffinity()
                finish()
            }
            .setNegativeButton("No", null).show()
    }
}