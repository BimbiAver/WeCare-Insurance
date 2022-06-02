package com.orionsoft.wecareinsurance

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orionsoft.wecareinsurance.model.Case
import com.orionsoft.wecareinsurance.model.Vehicle
import org.json.JSONException
import org.json.JSONObject

class PreviousCasesActivity : AppCompatActivity(), View.OnClickListener {

    private var caseNo = ""

    private lateinit var imgBtnPreCaseBack: ImageButton

    var listView: ListView? = null
    private lateinit var caseArrayList: ArrayList<Case>

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
            R.id.imgBtnPreCaseBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_cases)

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        imgBtnPreCaseBack = findViewById(R.id.imgBtnPreCaseBack)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnPreCaseBack.setOnClickListener(this)

        queue = Volley.newRequestQueue(this) // Instantiate the RequestQueue
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")

        // Initializing listview and caseArrayList
        listView = findViewById<View>(R.id.listViewPreCases) as ListView?
        caseArrayList = ArrayList()

        // Fetch cases from the database
        getPreviousCases(sharedPrefManager!!.getPreferenceString("nic").toString())

        progressDialog.show() // Show ProgressDialog

        // Fetch and load data to the ListView
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
            if (jsonObject != null) {
                loadCaseDetailsList() // Fetch and load data to the ListView
            }
        }, 3000)

        // ListView Items OnClick Menu - OnItemClickListener
        listView!!.onItemClickListener =
            AdapterView.OnItemClickListener() { adapterView, view, i, l ->
                val idView = view.findViewById<TextView>(R.id.txtPreCaseNo)
                caseNo = idView.text.toString()
                val intent = Intent(this, CaseDetailsActivity::class.java)
                // Bind case number
                intent.putExtra(
                    "caseNo",
                    caseNo
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

    // Get all the cases reported under the user's nic
    private fun getPreviousCases(nic: String) {
        stringRequest = object : StringRequest(
            Method.POST, urls.previousCases,
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

    private fun loadCaseDetailsList() {
        try {
            // We have the array inside the JSON object
            // So here we are getting that JSON array
            val caseArray = jsonObject!!.getJSONArray("Previous_Cases")
            // Now looping through all the elements of the JSON array
            for (i in 0 until caseArray.length()) {
                // Getting the JSON object of the particular index inside the array
                val caseObject = caseArray.getJSONObject(i)
                // Creating a case object and giving them the values from json object
                val case = Case(
                    caseObject.getString("case_no"),
                    caseObject.getString("case_status"),
                    caseObject.getString("vehicle_no"),
                    caseObject.getString("claim_no"),
                    caseObject.getString("accident_date"),
                    caseObject.getString("accident_time")
                )
                // Adding the case to caseDetailsList
                caseArrayList!!.add(case)
            }
            // Creating custom adapter object
            val caseListViewAdapter = CaseListViewAdapter(
                applicationContext,
                caseArrayList
            )
            // Adding the adapter to the ListView
            listView!!.setAdapter(caseListViewAdapter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}