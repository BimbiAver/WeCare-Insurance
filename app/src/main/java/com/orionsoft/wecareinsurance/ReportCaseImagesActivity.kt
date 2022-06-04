package com.orionsoft.wecareinsurance

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orionsoft.wecareinsurance.model.Case
import java.io.ByteArrayOutputStream


class ReportCaseImagesActivity : AppCompatActivity(), View.OnClickListener {

    private var img1 = ""
    private var img2 = ""
    private var img3 = ""

    private lateinit var imgBtnRCIBack: ImageButton
    private lateinit var btnRCIUpload: Button
    private lateinit var btnRCINext: Button

    private lateinit var imgViewRCIImg1: ImageView
    private lateinit var imgViewRCIImg2: ImageView
    private lateinit var imgViewRCIImg3: ImageView

    var case: Case? = null
    var case2: Case? = null

    private val PICK_IMAGE_REQUEST = 200
    private var bitmapImage: Bitmap? = null
    private var imagePath: Uri? = null

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnRCIBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.btnRCIUpload -> {
                imageChooser()
            }
            R.id.btnRCINext -> {
                if (img1 == "") {
                    Toast.makeText(applicationContext, "Please upload at least one image!", Toast.LENGTH_SHORT).show()
                } else {
                    case2 = Case(case!!.vehicleNo, case!!.mileage, case!!.coverNote, case!!.debitOut, case!!.driverNIC, case!!.driverLicenseNo, case!!.vehicleTypes, case!!.expiryDate, case!!.driverName, case!!.driverAddress, case!!.driverConNumber, case!!.accidentDate, case!!.accidentTime, case!!.location, case!!.damage, case!!.otherReason, case!!.extentDamage, img1, img2, img3)

                    val intent = Intent(this, ReportCaseThirdPartyActivity::class.java)
                    // Bind case object
                    intent.putExtra("objCase", case2)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_images)

        imgBtnRCIBack = findViewById(R.id.imgBtnRCIBack)
        btnRCIUpload = findViewById(R.id.btnRCIUpload)
        btnRCINext = findViewById(R.id.btnRCINext)
        imgViewRCIImg1 = findViewById(R.id.imgViewRCIImg1)
        imgViewRCIImg2 = findViewById(R.id.imgViewRCIImg2)
        imgViewRCIImg3 = findViewById(R.id.imgViewRCIImg3)

        // Instantiate the setOnClickListener(s) at runtime
        imgBtnRCIBack.setOnClickListener(this)
        btnRCIUpload.setOnClickListener(this)
        btnRCINext.setOnClickListener(this)

        // Retrieve data from the previous activity
        case = intent.getSerializableExtra("objCase") as? Case
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------

    private fun imageChooser() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                    // Assigning Image-1 URI to the ImageView
                    imagePath = data.clipData!!.getItemAt(0).uri
                    // Convert imageUri to bitmap
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imagePath)
                    // Assigning Image-1 URI to the ImageView
                    imgViewRCIImg1.setImageURI(imagePath)
                    img1 = getStringImage(bitmapImage!!)
                    imgViewRCIImg1.visibility = View.VISIBLE

                    // Assigning Image-2 URI to the ImageView
                    imagePath = data.clipData!!.getItemAt(1).uri
                    // Convert imageUri to bitmap
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imagePath)
                    // Assigning Image-2 URI to the ImageView
                    imgViewRCIImg2.setImageURI(imagePath)
                    img2 = getStringImage(bitmapImage!!)
                    imgViewRCIImg2.visibility = View.VISIBLE

            } else if (data?.getData() != null) {
                // if single image is selected

                imagePath = data.data!!
                bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imagePath)
                // Assigning your Image URI to the ImageViews
                imgViewRCIImg1.setImageURI(imagePath)
                imgViewRCIImg1.visibility = View.VISIBLE
                img1 = getStringImage(bitmapImage!!)
            }
        }
    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    //        -----------------------------------------------------------------------------------------------
}