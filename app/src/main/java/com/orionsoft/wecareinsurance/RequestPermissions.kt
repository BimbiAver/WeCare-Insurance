package com.orionsoft.wecareinsurance

import android.Manifest

class RequestPermissions {

    // Permissions request code
    val REQUEST_CODE_ASK_PERMISSIONS = 1

    // Permissions that need to be explicitly requested from end user
    val REQUIRED_SDK_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
}