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
import com.orionsoft.wecareinsurance.model.Case
import com.orionsoft.wecareinsurance.model.Policy
import org.json.JSONException
import org.json.JSONObject

class ReportCasePolicyActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var vehicleNo: String
    private lateinit var mileage: String

    private lateinit var imgBtnRCPBack: ImageButton
    private lateinit var btnRCPNext: Button

    private lateinit var edTxtRCPNumber: TextView
    private lateinit var edTxtRCPCoverPeriod: TextView
    private lateinit var edTxtRCPSumInsured: TextView
    private lateinit var edTxtRCPInsType: TextView

    private lateinit var raGroupRCPCoverNote: RadioGroup
    private lateinit var raBtnRCPCover: RadioButton
    private lateinit var raBtnRCPCoverNo: RadioButton

    private lateinit var raGroupRCPDebitOut: RadioGroup
    private lateinit var raBtnRCPDebit: RadioButton
    private lateinit var raBtnRCPDebitNo: RadioButton

    lateinit var progressDialog: ProgressDialog

    private val urls = URLs()
    private var queue : RequestQueue? = null // Volley RequestQueue
    private var stringRequest : StringRequest? = null // Volley StringRequest
    private var jsonObject : JSONObject? = null

    var policy: Policy? = null
    var case: Case? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCPBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.btnRCPNext -> {
                if (validateFields()) {
                    // Getting value from the selected RadioButtons
                    raBtnRCPCover = findViewById(raGroupRCPCoverNote.checkedRadioButtonId)
                    raBtnRCPDebit = findViewById(raGroupRCPDebitOut.checkedRadioButtonId)
                    case = Case(vehicleNo, mileage, raBtnRCPCover.text.toString(), raBtnRCPDebit.text.toString())

                    val intent = Intent(this, ReportCaseDriverDetailsActivity::class.java)
                    // Bind case object
                    intent.putExtra("objCase", case)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else {
                    Toast.makeText(
                        this,
                        "Whoops! Fields cannot be empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_policy)

        imgBtnRCPBack = findViewById(R.id.imgBtnRCPBack)
        btnRCPNext = findViewById(R.id.btnRCPNext)
        edTxtRCPNumber = findViewById(R.id.edTxtRCPNumber)
        edTxtRCPCoverPeriod = findViewById(R.id.edTxtRCPCoverPeriod)
        edTxtRCPSumInsured = findViewById(R.id.edTxtRCPSumInsured)
        edTxtRCPInsType = findViewById(R.id.edTxtRCPInsType)
        raGroupRCPCoverNote = findViewById(R.id.raGroupRCPCoverNote)
        raBtnRCPCoverNo = findViewById(R.id.raBtnRCPCoverNo)
        raGroupRCPDebitOut = findViewById(R.id.raGroupRCPDebitOut)
        raBtnRCPDebitNo = findViewById(R.id.raBtnRCPDebitNo)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCPBack.setOnClickListener(this)
        btnRCPNext.setOnClickListener(this)

        // Retrieve data from the previous activity
        vehicleNo = intent.getStringExtra("vehicleNo").toString()
        mileage = intent.getStringExtra("mileage").toString()

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Fetch the selected vehicle's insurance details from the database
        getInsuranceDetails(vehicleNo)

        progressDialog.show() // Show ProgressDialog

        // Fetch and load data to the ListView
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
            if (jsonObject != null) {
                setDataToFields() // Load fetched data to the TextViews
                btnRCPNext.isEnabled = true;
            }
        }, 3000)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------

    // Get insurance details
    private fun getInsuranceDetails(vehicleNo: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.getVehicleDetails,
            Response.Listener { response -> // response
                try {
                    jsonObject = JSONObject(response)

                    // Creating a new policy object
                    if (jsonObject!!.getString("message") == "Request Successfully Completed!") {
                        policy = Policy(
                            jsonObject!!.getString("policy_no"),
                            jsonObject!!.getString("cover_period"),
                            jsonObject!!.getString("sum_insured"),
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
                params["vehicleNo"] = vehicleNo
                return params
            }
        }
        queue!!.add(stringRequest)
    }

//        -----------------------------------------------------------------------------------------------

    // Update fields with fetched data
    private fun setDataToFields() {
        edTxtRCPNumber.setText("Policy Number: " + policy!!.policyNo)
        edTxtRCPCoverPeriod.setText("Period of Cover: " + policy!!.coverPeriod)
        edTxtRCPSumInsured.setText("Sum Insured: " + policy!!.sumInsured)
        edTxtRCPInsType.setText("Hire Purchase / Lease: " + policy!!.type)
    }

//        -----------------------------------------------------------------------------------------------

    // Validate fields
    private fun validateFields(): Boolean {
        if (raGroupRCPCoverNote.checkedRadioButtonId == -1) {
            raBtnRCPCoverNo.error = "Select item!"
        } else {
            raBtnRCPCoverNo.error = null
        }

        if (raGroupRCPDebitOut.checkedRadioButtonId == -1) {
            raBtnRCPDebitNo.error = "Select item!"
        } else {
            raBtnRCPDebitNo.error = null
        }

        // ---------------------------------------------------------------

        return raBtnRCPCoverNo.error == null && raBtnRCPDebitNo.error == null
    }
}