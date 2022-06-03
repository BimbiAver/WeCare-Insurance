package com.orionsoft.wecareinsurance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.orionsoft.wecareinsurance.model.Case
import java.text.SimpleDateFormat
import java.util.*

class ReportCaseDriverDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgBtnRCDBack: ImageButton
    private lateinit var btnRCDNext: Button

    private lateinit var chBoxRCDSameAs: CheckBox

    private lateinit var edTxtRCDNICNo: TextView
    private lateinit var edTxtRCDLicenseNo: TextView
    private lateinit var edTxtRCDVehiCat: TextView
    private lateinit var edTxtRCDExpDate: TextView
    private lateinit var edTxtRCDFName: TextView
    private lateinit var edTxtRCDAddress: TextView
    private lateinit var edTxtRCDConNumber: TextView

    val myCalendar = Calendar.getInstance()!!
    var date: OnDateSetListener? = null

    private var sharedPrefManager: SharedPrefManager? = null // SharedPreferences

    var case: Case? = null
    var case2: Case? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCDBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.edTxtRCDExpDate -> {
                showDatePickerDialog()
            }
            R.id.btnRCDNext -> {
                if (validateFields()) {
                    case2 = Case(case!!.vehicleNo, case!!.mileage, case!!.coverNote, case!!.debitOut, case!!.driverNIC, case!!.driverLicenseNo, case!!.vehicleTypes, case!!.expiryDate, case!!.driverName, case!!.driverAddress, case!!.driverConNumber)

                    val intent = Intent(this, ReportCaseDetailsActivity::class.java)
                    // Bind case object
                    intent.putExtra("objCase", case2)
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
        setContentView(R.layout.activity_report_case_driver_details)

        imgBtnRCDBack = findViewById(R.id.imgBtnRCDBack)
        btnRCDNext = findViewById(R.id.btnRCDNext)
        edTxtRCDNICNo = findViewById(R.id.edTxtRCDNICNo)
        edTxtRCDLicenseNo = findViewById(R.id.edTxtRCDLicenseNo)
        edTxtRCDVehiCat = findViewById(R.id.edTxtRCDVehiCat)
        edTxtRCDExpDate = findViewById(R.id.edTxtRCDExpDate)
        edTxtRCDFName = findViewById(R.id.edTxtRCDFName)
        edTxtRCDAddress = findViewById(R.id.edTxtRCDAddress)
        edTxtRCDConNumber = findViewById(R.id.edTxtRCDConNumber)
        chBoxRCDSameAs = findViewById(R.id.chBoxRCDSameAs)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCDBack.setOnClickListener(this)
        btnRCDNext.setOnClickListener(this)
        edTxtRCDExpDate.setOnClickListener(this)

        // Retrieve data from the previous activity
        case = intent.getSerializableExtra("objCase") as? Case

        sharedPrefManager = SharedPrefManager(this) // Instantiate the SharedPrefManager

        chBoxRCDSameAs.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                edTxtRCDNICNo.text = sharedPrefManager!!.getPreferenceString("nic")
                edTxtRCDLicenseNo.text = sharedPrefManager!!.getPreferenceString("licenseNo")
                edTxtRCDFName.text = sharedPrefManager!!.getPreferenceString("name")
                edTxtRCDAddress.text = sharedPrefManager!!.getPreferenceString("address")
                edTxtRCDConNumber.text = sharedPrefManager!!.getPreferenceString("mobNumber")
            } else {
                edTxtRCDNICNo.text = ""
                edTxtRCDLicenseNo.text = ""
                edTxtRCDFName.text = ""
                edTxtRCDAddress.text = ""
                edTxtRCDConNumber.text = ""
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------

    //    View DatePickerDialog
    private fun showDatePickerDialog() {
        date = OnDateSetListener { _, year, month, day ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            setDate()
        }
        DatePickerDialog(
            this, R.style.DatePickerDialog, date,
            myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    //    Update edTxtRCDExpDate field with expiry date
    private fun setDate() {
        val myFormat = "yyyy/MM/dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        edTxtRCDExpDate.text = dateFormat.format(myCalendar.time)
    }

//        -----------------------------------------------------------------------------------------------

    // Validate fields
    private fun validateFields(): Boolean {
        if (TextUtils.isEmpty(edTxtRCDNICNo.text.toString())) {
            edTxtRCDNICNo.error = "Field cannot be empty!"
        }

        if (TextUtils.isEmpty(edTxtRCDLicenseNo.text.toString())) {
            edTxtRCDLicenseNo.error = "Field cannot be empty!"
        }

        if (TextUtils.isEmpty(edTxtRCDVehiCat.text.toString())) {
            edTxtRCDVehiCat.error = "Field cannot be empty!"
        }

        if (TextUtils.isEmpty(edTxtRCDExpDate.text.toString())) {
            edTxtRCDExpDate.error = "Field cannot be empty!"
        } else {
            edTxtRCDExpDate.error = null
        }

        if (TextUtils.isEmpty(edTxtRCDFName.text.toString())) {
            edTxtRCDFName.error = "Field cannot be empty!"
        }

        if (TextUtils.isEmpty(edTxtRCDAddress.text.toString())) {
            edTxtRCDAddress.error = "Field cannot be empty!"
        }

        if (TextUtils.isEmpty(edTxtRCDConNumber.text.toString())) {
            edTxtRCDConNumber.error = "Field cannot be empty!"
        }

        // ---------------------------------------------------------------

        return edTxtRCDNICNo.error == null && edTxtRCDLicenseNo.error == null && edTxtRCDVehiCat.error == null && edTxtRCDFName.error == null && edTxtRCDAddress.error == null && edTxtRCDConNumber.error == null
    }
}