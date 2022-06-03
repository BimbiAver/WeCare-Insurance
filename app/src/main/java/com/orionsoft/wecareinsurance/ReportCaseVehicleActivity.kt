package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Vehicle
import org.json.JSONException
import org.json.JSONObject


class ReportCaseVehicleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgBtnRCVBack: ImageButton
    private lateinit var incldRCVDetails: View
    private lateinit var spnRCVVehicle: Spinner
    private lateinit var txtRCVType: TextView
    private lateinit var txtRCVMake: TextView
    private lateinit var txtRCVModel: TextView
    private lateinit var txtRCVManufacYear: TextView
    private lateinit var txtRCVTransmission: TextView
    private lateinit var txtRCVFuel: TextView
    private lateinit var txtRCVEngCapacity: TextView
    private lateinit var txtRCVEngNo: TextView
    private lateinit var txtRCVChassisNo: TextView
    private lateinit var txtRCVMileage: EditText
    private lateinit var btnRCVNext: Button

    private var vehicleNoArrayList: MutableList<String> = mutableListOf<String>()

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences for login session
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

    var vehicle: Vehicle? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCVBack -> {
                onBackPressed()
            }
            R.id.btnRCVNext -> {
                val mileage: String = txtRCVMileage.text.toString()
                val vehicleNo: String = spnRCVVehicle.selectedItem.toString()
                // Check whether the mileage is empty or not
                if (mileage.isEmpty()) {
                    txtRCVMileage.requestFocus()
                    txtRCVMileage.error = "Please enter the mileage!"
                } else {
                    val intent = Intent(this, ReportCasePolicyActivity::class.java)
                    // Bind vehicle number
                    intent.putExtra("vehicleNo", vehicleNo)
                    intent.putExtra("mileage", mileage)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_vehicle)

        imgBtnRCVBack = findViewById(R.id.imgBtnRCVBack)
        incldRCVDetails = findViewById(R.id.incldRCVDetails)
        spnRCVVehicle = findViewById(R.id.spnRCVVehicle)
        txtRCVType = findViewById(R.id.txtRCVType)
        txtRCVMake = findViewById(R.id.txtRCVMake)
        txtRCVModel = findViewById(R.id.txtRCVModel)
        txtRCVManufacYear = findViewById(R.id.txtRCVManufacYear)
        txtRCVTransmission = findViewById(R.id.txtRCVTransmission)
        txtRCVFuel = findViewById(R.id.txtRCVFuel)
        txtRCVEngCapacity = findViewById(R.id.txtRCVEngCapacity)
        txtRCVEngNo = findViewById(R.id.txtRCVEngNo)
        txtRCVChassisNo = findViewById(R.id.txtRCVChassisNo)
        txtRCVMileage = findViewById(R.id.txtRCVMileage)
        btnRCVNext = findViewById(R.id.btnRCVNext)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCVBack.setOnClickListener(this)
        btnRCVNext.setOnClickListener(this)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        vehicleNoArrayList.add("- Select -")

        // Fetch vehicle numbers from the database
        getVehicleNumbers(sharedPrefManager!!.getPreferenceString("nic").toString())

        progressDialog.show() // Show ProgressDialog

        // Fetch and load data to the ListView
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
            if (jsonObject != null) {
                loadVehicleNumbers() // Fetch and load data to the spnRCVVehicle
            }
        }, 3000)

        // spnRCVVehicle - onItemSelectedListener
        spnRCVVehicle?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if  (!spnRCVVehicle.selectedItem.toString().equals("- Select -")) {
                    // Fetch the selected vehicle details from the database
                    getVehicleDetails(spnRCVVehicle.selectedItem.toString())

                    progressDialog.show() // Show ProgressDialog

                    // Fetch and load data to the ListView
                    val handler = Handler()
                    handler.postDelayed({
                        progressDialog.dismiss()
                        if (jsonObject != null) {
                            incldRCVDetails.visibility = View.VISIBLE
                            setDataToFields() // Load fetched data to the TextViews
                            btnRCVNext.isEnabled = true;
                        }
                    }, 3000)
                }
            }

        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_exit)
            .setTitle("Confirm Cancel")
            .setMessage("Are you sure you would like to cancel?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            })
            .setNegativeButton("No", null).show()
    }

//        -----------------------------------------------------------------------------------------------

    // Get all the vehicle numbers under the user's nic
    private fun getVehicleNumbers(nic: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.getVehicles,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)

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

//        -----------------------------------------------------------------------------------------------

    private fun loadVehicleNumbers() {
        try {
            // We have the array inside the JSON object
            // So here we are getting that JSON array
            val vehicleArray = jsonObject!!.getJSONArray("Vehicle_Details")
            // Now looping through all the elements of the JSON array
            for (i in 0 until vehicleArray.length()) {
                // Getting the JSON object of the particular index inside the array
                val vehObject = vehicleArray.getJSONObject(i)
                // Creating a vehicle no variable and giving them the values from json object
                val vehicleNo = vehObject.getString("vehicle_no")
                // Adding the vehicleNo to the vehicleNoArrayList
                vehicleNoArrayList!!.add(vehicleNo)
            }

            // Bind vehicle numbers to the spnRCVVehicle
            val vehicleNoArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, vehicleNoArrayList
            )
            spnRCVVehicle.visibility = View.VISIBLE
            spnRCVVehicle.setAdapter(vehicleNoArrayAdapter)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

//        -----------------------------------------------------------------------------------------------

    // Get vehicle details
    private fun getVehicleDetails(vehicleNo: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.getVehicleDetails,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)

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
        txtRCVType.setText(vehicle!!.type)
        txtRCVMake.setText(vehicle!!.make)
        txtRCVModel.setText(vehicle!!.model)
        txtRCVManufacYear.setText(vehicle!!.manufacYear)
        txtRCVTransmission.setText(vehicle!!.transmission)
        txtRCVFuel.setText(vehicle!!.fuel)
        txtRCVEngCapacity.setText(vehicle!!.engCapacity)
        txtRCVEngNo.setText(vehicle!!.engNo)
        txtRCVChassisNo.setText(vehicle!!.chassisNo)
    }
}