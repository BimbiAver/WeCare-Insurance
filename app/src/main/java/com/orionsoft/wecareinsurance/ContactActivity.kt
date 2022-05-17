package com.orionsoft.wecareinsurance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ContactActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSupEmail: Button
    private lateinit var btnSupCall: Button
    private lateinit var imgBtnSupBack: ImageButton

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSupEmail -> {
                val intent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "hello@wecare.plus", null
                    )
                )
                intent.putExtra(Intent.EXTRA_SUBJECT, "WeCare Insurance - Support")
                startActivity(Intent.createChooser(intent, "Choose an Email client:"))
            }
            R.id.btnSupCall -> {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:+94774246925")
                startActivity(intent)
            }
            R.id.imgBtnSupBack -> {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

//        -----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        btnSupEmail = findViewById(R.id.btnSupEmail)
        btnSupCall = findViewById(R.id.btnSupCall)
        imgBtnSupBack = findViewById(R.id.imgBtnSupBack)

        // Instantiate the setOnClickListener(s) at runtime
        btnSupEmail.setOnClickListener(this)
        btnSupCall.setOnClickListener(this)
        imgBtnSupBack.setOnClickListener(this)
    }

//        -----------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}