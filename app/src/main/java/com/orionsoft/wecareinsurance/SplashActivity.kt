package com.orionsoft.wecareinsurance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {

    private var splashTime: Long = 1500
    private var usrCheckMsg = ""

    private val sharedPref = "AppStatus"

    private val checkNetwork = CheckNetwork()
    private val urls = URLs()

    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar and make the activity fullscreen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue

        // Call initial server request
        checkUser("abc@mail.com")

        //        ----------------------------------------------------------------------------------

        // Check to make sure it is connected to a network
        if (checkNetwork.checkInternet(this)) {
            // Redirect into the ExploreActivity after 2 seconds
            Handler().postDelayed({
                val settings = getSharedPreferences(sharedPref, 0)

                if (settings.getBoolean("firstTime", true)) {
                    // The app is being launched for first time, do something
                    val intent = Intent(this, ExploreActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()

                    // Record the fact that the app has been started at least once
                    settings.edit().putBoolean("firstTime", false).commit()
                } else {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }, splashTime)
        } else {
            Toast.makeText(
                this,
                "Check your internet connection and try again!",
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({
                finishAffinity() // Removes the connection of the existing activity to its stack
                finish() // The method onDestroy() is executed & exit the application

            }, 2000)
        }
    }

//        -----------------------------------------------------------------------------------------------

    private fun checkUser(emailAddress: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.usrCheck,
            Response.Listener { response -> // response
                try {
                    val jsonObject = JSONObject(response)
                    usrCheckMsg = jsonObject.getString("message")
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
                params["email"] = emailAddress
                return params
            }
        }
        queue!!.add(stringRequest)
    }
}