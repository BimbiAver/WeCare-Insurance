package com.orionsoft.wecareinsurance

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class ExploreActivity : AppCompatActivity() {

    lateinit private var btnGetStarted: Button
    val requestPermissions = RequestPermissions();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        btnGetStarted = findViewById(R.id.btnGetStarted)

        btnGetStarted.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        checkPermissions() // Request permissions
    }

//        -----------------------------------------------------------------------------------------------

    // Checks the dynamically-controlled permissions and requests missing permissions from end user
    protected fun checkPermissions() {
        val missingPermissions: MutableList<String> = ArrayList()
        // check all required dynamic permissions
        for (permission in requestPermissions.REQUIRED_SDK_PERMISSIONS) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission)
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            val permissions = missingPermissions
                .toTypedArray()
            ActivityCompat.requestPermissions(this, permissions, requestPermissions.REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            val grantResults = IntArray(requestPermissions.REQUIRED_SDK_PERMISSIONS.size)
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
            onRequestPermissionsResult(
                requestPermissions.REQUEST_CODE_ASK_PERMISSIONS, requestPermissions.REQUIRED_SDK_PERMISSIONS,
                grantResults
            )
        }
    }
}