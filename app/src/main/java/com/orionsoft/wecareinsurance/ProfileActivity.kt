package com.orionsoft.wecareinsurance

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgBtnProfBack: ImageButton
    private lateinit var txtProfNIC: TextView
    private lateinit var txtProfLicenseNo: TextView
    private lateinit var txtProfName: TextView
    private lateinit var txtProfDOB: TextView
    private lateinit var txtProfGender: TextView
    private lateinit var txtProfAddress: TextView
    private lateinit var txtProfMobNum: TextView

    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences for login session

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnProfBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imgBtnProfBack = findViewById(R.id.imgBtnProfBack)
        txtProfNIC = findViewById(R.id.txtProfNIC)
        txtProfLicenseNo = findViewById(R.id.txtProfLicenseNo)
        txtProfName = findViewById(R.id.txtProfName)
        txtProfDOB = findViewById(R.id.txtProfDOB)
        txtProfGender = findViewById(R.id.txtProfGender)
        txtProfAddress = findViewById(R.id.txtProfAddress)
        txtProfMobNum = findViewById(R.id.txtProfMobNum)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        txtProfNIC.text = sharedPrefManager!!.getPreferenceString("nic")
        txtProfLicenseNo.text = sharedPrefManager!!.getPreferenceString("licenseNo")
        txtProfName.text = sharedPrefManager!!.getPreferenceString("name")
        txtProfDOB.text = sharedPrefManager!!.getPreferenceString("dob")
        txtProfGender.text = sharedPrefManager!!.getPreferenceString("gender")
        txtProfAddress.text = sharedPrefManager!!.getPreferenceString("address")
        txtProfMobNum.text = sharedPrefManager!!.getPreferenceString("mobNumber")

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnProfBack.setOnClickListener(this)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}