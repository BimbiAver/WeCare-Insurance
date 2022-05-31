package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Vehicle
import org.json.JSONException
import org.json.JSONObject

class VehicleDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgBtnVehDetailsBack: ImageButton

    private lateinit var txtVDVehNo: TextView
    private lateinit var txtVDType: TextView
    private lateinit var txtVDMake: TextView
    private lateinit var txtVDModel: TextView
    private lateinit var txtVDManufacYear: TextView
    private lateinit var txtVDTransmission: TextView
    private lateinit var txtVDFuel: TextView
    private lateinit var txtVDEngCapacity: TextView
    private lateinit var txtVDEngNo: TextView
    private lateinit var txtVDChassisNo: TextView

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

    var vehicle: Vehicle? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnVehDetailsBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)

        imgBtnVehDetailsBack = findViewById(R.id.imgBtnVehDetailsBack)
        txtVDVehNo = findViewById(R.id.txtVDVehNo)
        txtVDType = findViewById(R.id.txtVDType)
        txtVDMake = findViewById(R.id.txtVDMake)
        txtVDModel = findViewById(R.id.txtVDModel)
        txtVDManufacYear = findViewById(R.id.txtVDManufacYear)
        txtVDTransmission = findViewById(R.id.txtVDTransmission)
        txtVDFuel = findViewById(R.id.txtVDFuel)
        txtVDEngCapacity = findViewById(R.id.txtVDEngCapacity)
        txtVDEngNo = findViewById(R.id.txtVDEngNo)
        txtVDChassisNo = findViewById(R.id.txtVDChassisNo)

        // Retrieve vehicleNo from previous activity
        var vehicleNo = intent.getStringExtra("vehicleNo").toString()

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnVehDetailsBack.setOnClickListener(this)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Fetch vehicle details
        getVehicleDetails(vehicleNo)

        progressDialog.show() // Show ProgressDialog

        // Fetch and load data to the ListView
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
            if (jsonObject != null) {
                setDataToFields(); // Load fetched data to the fields
            } else {
                Toast.makeText(
                    this,
                    "Something unexpected happened!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, 3000)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------

    // Get vehicle details
    private fun getVehicleDetails(vehicleNo: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.getVehicleDetails,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)

                    // Creating a new user object

                    // Creating a new vehicle object
                    if (jsonObject!!.getString("message") == "Request Successfully Completed!") {
                        vehicle = Vehicle(
                            jsonObject!!.getString("vehicle_no"),
                            jsonObject!!.getString("vehicle_type"),
                            jsonObject!!.getString("make"),
                            jsonObject!!.getString("model"),
                            jsonObject!!.getString("manufac_year"),
                            jsonObject!!.getString("transmission"),
                            jsonObject!!.getString("fuel"),
                            jsonObject!!.getString("eng_capacity"),
                            jsonObject!!.getString("eng_no"),
                            jsonObject!!.getString("chassis_no")
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
                params["vehicleNo"] = vehicleNo
                return params
            }
        }
        queue!!.add(stringRequest)
    }

//        -----------------------------------------------------------------------------------------------

    // Update fields with fetched data
    private fun setDataToFields() {
        txtVDVehNo.setText(vehicle!!.vehicleNo)
        txtVDType.setText(vehicle!!.type)
        txtVDMake.setText(vehicle!!.make)
        txtVDModel.setText(vehicle!!.model)
        txtVDManufacYear.setText(vehicle!!.manufacYear)
        txtVDTransmission.setText(vehicle!!.transmission)
        txtVDFuel.setText(vehicle!!.fuel)
        txtVDEngCapacity.setText(vehicle!!.engCapacity)
        txtVDEngNo.setText(vehicle!!.engNo)
        txtVDChassisNo.setText(vehicle!!.chassisNo)
    }
}