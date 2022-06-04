package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Case
import com.orionsoft.wecareinsurance.model.Policy
import org.json.JSONException
import org.json.JSONObject

class ReportCaseThirdPartyActivity : AppCompatActivity(), View.OnClickListener {

    var caseCheckMsg = ""

    private lateinit var imgBtnRCTPBack: ImageButton
    private lateinit var btnRCTPSubmit: Button

    private lateinit var edTxtRCTPVehicleNo: TextView
    private lateinit var edTxtRCTPBrand: TextView
    private lateinit var edTxtRCTPDrivName: TextView
    private lateinit var edTxtRCTPOwnName: TextView
    private lateinit var edTxtRCTPAddress: TextView
    private lateinit var edTxtRCTPDamages: TextView
    private lateinit var edTxtRCTPOtherDetails: TextView

    var case: Case? = null
    var case2: Case? = null

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCTPBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.btnRCTPSubmit -> {
                case2 = Case(case!!.vehicleNo, case!!.mileage, case!!.coverNote, case!!.debitOut, case!!.driverNIC, case!!.driverLicenseNo, case!!.vehicleTypes, case!!.expiryDate, case!!.driverName, case!!.driverAddress, case!!.driverConNumber, case!!.accidentDate, case!!.accidentTime, case!!.location, case!!.damage, case!!.otherReason, case!!.extentDamage, case!!.image1, case!!.image2, case!!.image3, edTxtRCTPVehicleNo.text.toString(), edTxtRCTPBrand.text.toString(), edTxtRCTPDrivName.text.toString(), edTxtRCTPOwnName.text.toString(), edTxtRCTPAddress.text.toString(), edTxtRCTPDamages.text.toString(), edTxtRCTPOtherDetails.text.toString())

                submitCase() // Submit case
                progressDialog.show()

                val handler = Handler()
                handler.postDelayed({
                    progressDialog.dismiss()

                    if (caseCheckMsg == "Case Added Successfully!") {
//                        Toast.makeText(this,"Case Added Successfully!", Toast.LENGTH_SHORT).show()
                        val i = Intent(applicationContext, ReportCaseSubmitActivity::class.java)
                        startActivity(i)
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        Toast.makeText(this,"Something unexpected happened!", Toast.LENGTH_SHORT).show()
                    }
                }, 7500)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_third_party)

        imgBtnRCTPBack = findViewById(R.id.imgBtnRCTPBack)
        btnRCTPSubmit = findViewById(R.id.btnRCTPSubmit)
        edTxtRCTPVehicleNo = findViewById(R.id.edTxtRCTPVehicleNo)
        edTxtRCTPBrand = findViewById(R.id.edTxtRCTPBrand)
        edTxtRCTPDrivName = findViewById(R.id.edTxtRCTPDrivName)
        edTxtRCTPOwnName = findViewById(R.id.edTxtRCTPOwnName)
        edTxtRCTPAddress = findViewById(R.id.edTxtRCTPAddress)
        edTxtRCTPDamages = findViewById(R.id.edTxtRCTPDamages)
        edTxtRCTPOtherDetails = findViewById(R.id.edTxtRCTPOtherDetails)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCTPBack.setOnClickListener(this)
        btnRCTPSubmit.setOnClickListener(this)

        // Retrieve data from the previous activity
        case = intent.getSerializableExtra("objCase") as? Case

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------

    // Submit case
    private fun submitCase() {
        stringRequest = object : StringRequest(
            Method.POST, urls.submitCase,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)
                    caseCheckMsg = jsonObject!!.getString("message")

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
                params["vehicleNo"] = case2!!.vehicleNo
                params["mileage"] = case2!!.mileage
                params["coverNote"] = case2!!.coverNote
                params["debitOut"] = case2!!.debitOut
                params["driverdriverNIC"] = case2!!.driverNIC
                params["driverLicenseNo"] = case2!!.driverLicenseNo
                params["vehicleTypes"] = case2!!.vehicleTypes
                params["expiryDate"] = case2!!.expiryDate
                params["driverName"] = case2!!.driverName
                params["driverAddress"] = case2!!.driverAddress
                params["driverConNumber"] = case2!!.driverConNumber
                params["accidentDate"] = case2!!.accidentDate
                params["accidentTime"] = case2!!.accidentTime
                params["location"] = case2!!.location
                params["damage"] = case2!!.damage
                params["otherReason"] = case2!!.otherReason
                params["extentDamage"] = case2!!.extentDamage
                params["image1"] = case2!!.image1
                params["image2"] = case2!!.image2
                params["image3"] = case2!!.image3
                params["tpVehicleNo"] = case2!!.tpVehicleNo
                params["tpBrand"] = case2!!.tpVehicleBrandModel
                params["tpDrivName"] = case2!!.tpDriverName
                params["tpOwnName"] = case2!!.tpOwnerName
                params["tpAddress"] = case2!!.tpAddress
                params["tpDamages"] = case2!!.tpDamages
                params["tpOtherDetails"] = case2!!.tpOtherDetails
                return params
            }
        }
        queue!!.add(stringRequest)
    }
}