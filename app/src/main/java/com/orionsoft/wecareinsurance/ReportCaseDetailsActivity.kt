package com.orionsoft.wecareinsurance

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.orionsoft.wecareinsurance.model.Case
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReportCaseDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private var date = ""
    private var time = ""
    private var tempLatitude = ""
    private var tempLongitude = ""

    private lateinit var imgBtnRCDetBack: ImageButton
    private lateinit var btnRCDetNext: Button

    private lateinit var edTxtRCDetDate: TextView
    private lateinit var edTxtRCDetTime: TextView
    private lateinit var edTxtRCDetLocation: TextView
    private lateinit var edTxtRCDetOtherReason: TextView
    private lateinit var edTxtRCDetExtDamage: TextView

    private lateinit var spnRCDetCauseOfDmg: Spinner

    var case: Case? = null
    var case2: Case? = null

    // Initializing FusedLocationProviderClient object
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var PERMISSION_ID = 44

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCDetBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.btnRCDetNext -> {
                case2 = Case(case!!.vehicleNo, case!!.mileage, case!!.coverNote, case!!.debitOut, case!!.driverNIC, case!!.driverLicenseNo, case!!.vehicleTypes, case!!.expiryDate, case!!.driverName, case!!.driverAddress, case!!.driverConNumber, edTxtRCDetDate.text.toString(), edTxtRCDetTime.text.toString(), edTxtRCDetLocation.text.toString(), spnRCDetCauseOfDmg.selectedItem.toString(), edTxtRCDetOtherReason.text.toString(), edTxtRCDetExtDamage.text.toString())

                val intent = Intent(this, ReportCaseImagesActivity::class.java)
                // Bind case object
                intent.putExtra("objCase", case2)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_details)

        imgBtnRCDetBack = findViewById(R.id.imgBtnRCDetBack)
        btnRCDetNext = findViewById(R.id.btnRCDetNext)
        edTxtRCDetDate = findViewById(R.id.edTxtRCDetDate)
        edTxtRCDetTime = findViewById(R.id.edTxtRCDetTime)
        edTxtRCDetLocation = findViewById(R.id.edTxtRCDetLocation)
        edTxtRCDetOtherReason = findViewById(R.id.edTxtRCDetOtherReason)
        edTxtRCDetExtDamage = findViewById(R.id.edTxtRCDetExtDamage)
        spnRCDetCauseOfDmg = findViewById(R.id.spnRCDetCauseOfDmg)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCDetBack.setOnClickListener(this)
        btnRCDetNext.setOnClickListener(this)

        // Retrieve data from the previous activity
        case = intent.getSerializableExtra("objCase") as? Case

        // Get system's current date and time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
        date = dateFormat.format(Calendar.getInstance().time)
        time = timeFormat.format(Calendar.getInstance().time)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Method to get the location
        getLastLocation()

        setDataOnStartup() // Update fields with data on startup
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // Check if permissions are given
        if (checkPermissions()) {

            // Check if location is enabled
            if (isLocationEnabled()) {

                // Getting last location from FusedLocationClient object
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        tempLatitude = location.latitude.toString()
                        tempLongitude = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // If permissions aren't available, request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            tempLatitude = mLastLocation.latitude.toString()
            tempLongitude = mLastLocation.longitude.toString()
        }
    }

    // Method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // Method to check if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }

//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------

    // Update fields with data on startup
    private fun setDataOnStartup() {
        edTxtRCDetDate.text = date
        edTxtRCDetTime.text = time
        val handler = Handler()
        handler.postDelayed({ edTxtRCDetLocation.text = "$tempLatitude,$tempLongitude" }, 3000)
    }
}