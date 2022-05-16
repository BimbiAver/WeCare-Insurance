package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Customer
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var usrCheckMsg = ""
    private lateinit var verificationCode: String

    private lateinit var btnLogin: Button
    private lateinit var txtLoginHelp: TextView
    private lateinit var txtMobOTPMsg: TextView
    private lateinit var edTxtLoginNIC: EditText
    private lateinit var edTxtLoginVerify: EditText

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private val generateVerificationCode = GenerateVerificationCode()

    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences for login session
    var customer: Customer? = null
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                val btnText: String = btnLogin.text.toString()
                val usrNIC: String = edTxtLoginNIC.text.toString()
                val mobVerify: String = edTxtLoginVerify.text.toString()
                if (usrNIC.isEmpty()) {
                    edTxtLoginNIC.error = "Please enter your NIC number!"
                } else if (usrNIC.length < 10) {
                    edTxtLoginNIC.error = "Please enter a valid NIC number!"
                } else if (usrNIC.isNotEmpty() && btnText == "Next") {
                    checkUser(usrNIC) // Check whether the user is already a customer
                    progressDialog.show()

                    Handler().postDelayed({
                        progressDialog.dismiss()
                        if (usrCheckMsg == "User found!") {
                            sendSMS(customer!!.mobNumber, verificationCode)
                            txtMobOTPMsg.text = "Enter the OTP sent to +94" + customer!!.mobNumber.substring(1)
                            txtMobOTPMsg.visibility = View.VISIBLE
                            edTxtLoginVerify.visibility = View.VISIBLE
                            edTxtLoginVerify.requestFocus()
                            btnLogin.setText("Log In")
                        } else {
                            Toast.makeText(
                                this,
                                "No user with this NIC number has been found!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, 2000)
                }

                //  ----------------- Login --------------
                if (mobVerify.isEmpty() && btnText == "Log In") {
                    edTxtLoginVerify.error = "Please enter the verification code!"
                } else if (usrNIC.isNotEmpty() && mobVerify.isNotEmpty() && btnText == "Log In") {
                    if (mobVerify == verificationCode) {
                        // Session management and redirection
                        // Storing the user in shared preferences
                        sharedPrefManager!!.save_String("nic",customer!!.nic)
                        sharedPrefManager!!.save_String("licenseNo",customer!!.licenseNo)
                        sharedPrefManager!!.save_String("name",customer!!.name)
                        sharedPrefManager!!.save_String("dob",customer!!.dob)
                        sharedPrefManager!!.save_String("gender",customer!!.gender)
                        sharedPrefManager!!.save_String("address",customer!!.address)
                        sharedPrefManager!!.save_String("mobNumber",customer!!.mobNumber)
                        sharedPrefManager!!.save_String("loginStatus","1")
                        // Direct to the Dashboard activity
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    } else {
                        edTxtLoginVerify.error = "Provided verification code is invalid!"
                        Toast.makeText(
                            this,
                            "Provided verification code is invalid!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            R.id.txtLoginHelp -> {
                val intent = Intent(this, ContactActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        if (sharedPrefManager!!.getPreferenceString("loginStatus") != null){
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        btnLogin = findViewById(R.id.btnLogin)
        txtLoginHelp = findViewById(R.id.txtLoginHelp)
        edTxtLoginNIC = findViewById(R.id.edTxtLoginNIC)
        edTxtLoginVerify = findViewById(R.id.edTxtLoginVerify)
        txtMobOTPMsg = findViewById(R.id.txtMobOTPMsg)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        verificationCode = generateVerificationCode.generateCode()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Instantiate the setOnClickListener(s) at runtime
        btnLogin.setOnClickListener(this)
        txtLoginHelp.setOnClickListener(this)
    }

//        -----------------------------------------------------------------------------------------------

    //        -----------------------------------------------------------------------------------------------
    override fun onBackPressed() {
        finishAffinity() // Removes the connection of the existing activity to its stack
        finish() // The method onDestroy() is executed & exit the application
    }

//        -----------------------------------------------------------------------------------------------

    // Send verification code
    private fun sendSMS(mobNumber: String, verificationCode: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.sendSMS,
            Response.Listener { response -> // response
            },
            Response.ErrorListener { error -> // error
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["to"] = mobNumber.substring(1)
                params["msg"] = "Your WeCare verification code is: $verificationCode"
                return params
            }
        }
        queue!!.add(stringRequest)
    }

//        -----------------------------------------------------------------------------------------------

    // Check whether the user is already a customer
    private fun checkUser(nic: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.usrCheck,
            Response.Listener { response -> // response
                try {
                    val jsonObject = JSONObject(response)
                    usrCheckMsg = jsonObject.getString("message")

                    // Creating a new user object
                    if (jsonObject.getString("message") == "User found!") {
                        customer = Customer(
                            jsonObject.getJSONObject("user").getString("nic"),
                            jsonObject.getJSONObject("user").getString("license_no"),
                            jsonObject.getJSONObject("user").getString("name"),
                            jsonObject.getJSONObject("user").getString("dob"),
                            jsonObject.getJSONObject("user").getString("gender"),
                            jsonObject.getJSONObject("user").getString("address"),
                            jsonObject.getJSONObject("user").getString("mob_number")
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> // error
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["nic"] = nic
                return params
            }
        }
        queue!!.add(stringRequest)
    }
}