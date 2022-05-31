package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Vehicle
import org.json.JSONException
import org.json.JSONObject

class RegisteredVehiclesActivity : AppCompatActivity(), View.OnClickListener {

    private var vehicleNo = ""

    private lateinit var imgBtnRegVehBack: ImageButton

    var listView: ListView? = null
    private lateinit var vehicleArrayList: ArrayList<Vehicle>

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences for login session
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRegVehBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_vehicles)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        imgBtnRegVehBack = findViewById(R.id.imgBtnRegVehBack)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRegVehBack.setOnClickListener(this)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Initializing listview and vehicleArrayList
        listView = findViewById<View>(R.id.listViewRegVehicles) as ListView?
        vehicleArrayList = ArrayList()

        // Fetch vehicles from the database
        getVehicles(sharedPrefManager!!.getPreferenceString("nic").toString())

        progressDialog.show() // Show ProgressDialog

        // Fetch and load data to the ListView
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
            if (jsonObject != null) {
                loadVehicleDetailsList() // Fetch and load data to the ListView
            }
        }, 3000)

        // ListView Items LongPress Menu - OnItemLongClickListener
        listView!!.onItemClickListener =
            AdapterView.OnItemClickListener() { adapterView, view, i, l ->
                val idView = view.findViewById<TextView>(R.id.txtRegVehNo)
                vehicleNo = idView.text.toString()
                val intent = Intent(this, VehicleDetailsActivity::class.java)
                // Bind vehicle number
                intent.putExtra(
                    "vehicleNo",
                    vehicleNo
                )
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                false
            }

    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------

    // Get all the vehicles registered under the user's nic
    private fun getVehicles(nic: String) {
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

    private fun loadVehicleDetailsList() {
        try {
            // We have the array inside the JSON object
            // So here we are getting that JSON array
            val vehicleArray = jsonObject!!.getJSONArray("Vehicle_Details")
            // Now looping through all the elements of the JSON array
            for (i in 0 until vehicleArray.length()) {
                // Getting the JSON object of the particular index inside the array
                val vehObject = vehicleArray.getJSONObject(i)
                // Creating a vehicle object and giving them the values from json object
                val vehicle = Vehicle(
                    vehObject.getString("vehicle_no"),
                    vehObject.getString("vehicle_type"),
                    vehObject.getString("make"),
                    vehObject.getString("model"),
                    vehObject.getString("manufac_year"),
                    vehObject.getString("transmission"),
                    vehObject.getString("fuel"),
                    vehObject.getString("eng_capacity"),
                    vehObject.getString("eng_no"),
                    vehObject.getString("chassis_no")
                )
                // Adding the vehicle to vehicleDetailsList
                vehicleArrayList!!.add(vehicle)
            }
            // Creating custom adapter object
            val vehicleListViewAdapter = VehicleListViewAdapter(
                applicationContext,
                vehicleArrayList
            )
            // Adding the adapter to the ListView
            listView!!.setAdapter(vehicleListViewAdapter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}