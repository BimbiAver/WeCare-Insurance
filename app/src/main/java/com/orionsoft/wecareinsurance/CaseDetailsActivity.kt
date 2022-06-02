package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Case
import com.orionsoft.wecareinsurance.model.Policy
import org.json.JSONException
import org.json.JSONObject

class CaseDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgBtnCDBack: ImageButton

    private lateinit var txtCDCaseNo: TextView
    private lateinit var txtCDStatus: TextView
    private lateinit var txtCDDateTime: TextView
    private lateinit var txtCDVehicleNo: TextView
    private lateinit var txtCDMileage: TextView
    private lateinit var txtCDLocation: TextView
    private lateinit var txtCDDamage: TextView
    private lateinit var txtCDOtherReason: TextView
    private lateinit var txtCDExtDamage: TextView
    private lateinit var txtCDPolicyNo: TextView
    private lateinit var txtCDClaimNo: TextView
    private lateinit var txtCDCoverPeriod: TextView
    private lateinit var txtCDSumInsured: TextView
    private lateinit var txtCDInsType: TextView
    private lateinit var txtCDCoverNote: TextView
    private lateinit var txtCDDebtOut: TextView
    private lateinit var txtCDDrivNIC: TextView
    private lateinit var txtCDDrivLicenseNo: TextView
    private lateinit var txtCDDrivVehCat: TextView
    private lateinit var txtCDDrivLicenseExp: TextView
    private lateinit var txtCDDrivName: TextView
    private lateinit var txtCDDrivAddress: TextView
    private lateinit var txtCDDrivConNumber: TextView
    private lateinit var txtCDTPVehNo: TextView
    private lateinit var txtCDTPVehBrand: TextView
    private lateinit var txtCDTPDrivName: TextView
    private lateinit var txtCDTPOwnName: TextView
    private lateinit var txtCDTPAddress: TextView
    private lateinit var txtCDTPDamages: TextView
    private lateinit var txtCDTPOtherDetails: TextView

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

    var case: Case? = null
    var policy: Policy? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnCDBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_details)

        imgBtnCDBack = findViewById(R.id.imgBtnCDBack)
        txtCDCaseNo = findViewById(R.id.txtCDCaseNo)
        txtCDStatus = findViewById(R.id.txtCDStatus)
        txtCDDateTime = findViewById(R.id.txtCDDateTime)
        txtCDVehicleNo = findViewById(R.id.txtCDVehicleNo)
        txtCDMileage = findViewById(R.id.txtCDMileage)
        txtCDLocation = findViewById(R.id.txtCDLocation)
        txtCDDamage = findViewById(R.id.txtCDDamage)
        txtCDOtherReason = findViewById(R.id.txtCDOtherReason)
        txtCDExtDamage = findViewById(R.id.txtCDExtDamage)
        txtCDPolicyNo = findViewById(R.id.txtCDPolicyNo)
        txtCDClaimNo = findViewById(R.id.txtCDClaimNo)
        txtCDCoverPeriod = findViewById(R.id.txtCDCoverPeriod)
        txtCDSumInsured = findViewById(R.id.txtCDSumInsured)
        txtCDInsType = findViewById(R.id.txtCDInsType)
        txtCDCoverNote = findViewById(R.id.txtCDCoverNote)
        txtCDDebtOut = findViewById(R.id.txtCDDebtOut)
        txtCDDrivNIC = findViewById(R.id.txtCDDrivNIC)
        txtCDDrivLicenseNo = findViewById(R.id.txtCDDrivLicenseNo)
        txtCDDrivVehCat = findViewById(R.id.txtCDDrivVehCat)
        txtCDDrivLicenseExp = findViewById(R.id.txtCDDrivLicenseExp)
        txtCDDrivName = findViewById(R.id.txtCDDrivName)
        txtCDDrivAddress = findViewById(R.id.txtCDDrivAddress)
        txtCDDrivConNumber = findViewById(R.id.txtCDDrivConNumber)
        txtCDTPVehNo = findViewById(R.id.txtCDTPVehNo)
        txtCDTPVehBrand = findViewById(R.id.txtCDTPVehBrand)
        txtCDTPDrivName = findViewById(R.id.txtCDTPDrivName)
        txtCDTPOwnName = findViewById(R.id.txtCDTPOwnName)
        txtCDTPAddress = findViewById(R.id.txtCDTPAddress)
        txtCDTPDamages = findViewById(R.id.txtCDTPDamages)
        txtCDTPOtherDetails = findViewById(R.id.txtCDTPOtherDetails)

        // Retrieve vehicleNo from previous activity
        var caseNo = intent.getStringExtra("caseNo").toString()

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnCDBack.setOnClickListener(this)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Fetch case details
        fetchCaseDetails(caseNo)

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

    // Get case details
    private fun fetchCaseDetails(caseNo: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.caseDetails,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)

                    // Creating a new case object
                    if (jsonObject!!.getString("message") == "Request Successfully Completed!") {
                        case = Case(
                            jsonObject!!.getString("caseNo"),
                            jsonObject!!.getString("caseStatus"),
                            jsonObject!!.getString("vehicleNo"),
                            jsonObject!!.getString("mileage"),
                            jsonObject!!.getString("claimNo"),
                            jsonObject!!.getString("coverNote"),
                            jsonObject!!.getString("debitOut"),
                            jsonObject!!.getString("driverNic"),
                            jsonObject!!.getString("driverLicenseNo"),
                            jsonObject!!.getString("vehicleCategories"),
                            jsonObject!!.getString("expiryDate"),
                            jsonObject!!.getString("driverName"),
                            jsonObject!!.getString("driverAddress"),
                            jsonObject!!.getString("driverConNumber"),
                            jsonObject!!.getString("accidentDate"),
                            jsonObject!!.getString("accidentTime"),
                            jsonObject!!.getString("location"),
                            jsonObject!!.getString("damage"),
                            jsonObject!!.getString("otherReason"),
                            jsonObject!!.getString("extentDamage"),
                            "",
                            "",
                            "",
                            jsonObject!!.getString("caseNo"),
                            jsonObject!!.getString("caseStatus"),
                            jsonObject!!.getString("vehicleNo"),
                            jsonObject!!.getString("mileage"),
                            jsonObject!!.getString("claimNo"),
                            jsonObject!!.getString("coverNote"),
                            jsonObject!!.getString("debitOut")
                        )

                        // Creating a new policy object
                        policy = Policy(
                            jsonObject!!.getString("policyNo"),
                            jsonObject!!.getString("coverPeriod"),
                            jsonObject!!.getString("sumInsured"),
                            jsonObject!!.getString("type")
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
                params["caseNo"] = caseNo
                return params
            }
        }
        queue!!.add(stringRequest)
    }

//        -----------------------------------------------------------------------------------------------

    // Update fields with fetched data
    private fun setDataToFields() {
        txtCDCaseNo.setText(case!!.caseNo)
        txtCDStatus.setText(case!!.status)
        val status = when (case!!.status) {
            "Attended" -> txtCDStatus.setTextColor(Color.parseColor("#0000FF"))
            "Non-Attended" -> txtCDStatus.setTextColor(Color.parseColor("#FFA500"))
            "Completed" -> txtCDStatus.setTextColor(Color.parseColor("#00FF00"))
            "Rejected" -> txtCDStatus.setTextColor(Color.parseColor("#FF0000"))
            else -> ""
        }
        txtCDDateTime.setText(case!!.accidentDate + " " + case!!.accidentTime)
        txtCDVehicleNo.setText(case!!.vehicleNo)
        txtCDMileage.setText(case!!.mileage)
        txtCDLocation.setText(case!!.location)
        txtCDDamage.setText(case!!.damage)
        txtCDOtherReason.setText(case!!.otherReason)
        txtCDExtDamage.setText(case!!.extentDamage)
        txtCDPolicyNo.setText(policy!!.policyNo)
        txtCDClaimNo.setText(case!!.claimNo)
        txtCDCoverPeriod.setText(policy!!.coverPeriod)
        txtCDSumInsured.setText(policy!!.sumInsured)
        txtCDInsType.setText(policy!!.type)
        txtCDCoverNote.setText(case!!.coverNote)
        txtCDDebtOut.setText(case!!.debitOut)
        txtCDDrivNIC.setText(case!!.driverNIC)
        txtCDDrivLicenseNo.setText(case!!.driverLicenseNo)
        txtCDDrivVehCat.setText(case!!.vehicleTypes)
        txtCDDrivLicenseExp.setText(case!!.expiryDate)
        txtCDDrivName.setText(case!!.driverName)
        txtCDDrivAddress.setText(case!!.driverAddress)
        txtCDDrivConNumber.setText(case!!.driverConNumber)
        txtCDTPVehNo.setText(case!!.tpVehicleNo)
        txtCDTPVehBrand.setText(case!!.tpVehicleBrandModel)
        txtCDTPDrivName.setText(case!!.tpDriverName)
        txtCDTPOwnName.setText(case!!.tpOwnerName)
        txtCDTPAddress.setText(case!!.tpAddress)
        txtCDTPDamages.setText(case!!.tpDamages)
        txtCDTPOtherDetails.setText(case!!.tpOtherDetails)
    }
}