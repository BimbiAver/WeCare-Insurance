//package com.orionsoft.wecareinsurance
//
//import android.content.Context
//import android.content.Intent
//import com.orionsoft.wecareinsurance.model.Customer
//
//class SharedPrefManager {
//
//    // The constants
//    private val sharedPref = "UserSharedPref"
//    private val nic = "keyNIC"
//    private val licenseNo = "keyFName"
//    private val name = "keyLName"
//    private val mobNumber = "keyMobNumber"
//
//    private var mInstance: SharedPrefManager? = null
//    private var ctx: Context? = null
//
//    private fun SharedPrefManager(context: Context) {
//        ctx = context
//    }
//
//    @Synchronized
//    fun getInstance(context: Context): SharedPrefManager? {
//        if (mInstance == null) {
//            mInstance = SharedPrefManager(context)
//        }
//        return mInstance
//    }
//
//    // This method will store the user data in shared preferences
//    fun userLogin(customer: Customer) {
//        val sharedPreferences = ctx!!.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString(nic, customer.nic)
//        editor.putString(licenseNo, customer.licenseNo)
//        editor.putString(name, customer.name)
//        editor.putString(mobNumber, customer.mobNumber)
//        editor.apply()
//    }
//
//    // This method will checker whether user is already logged in or not
//    fun isLoggedIn(): Boolean {
//        val sharedPreferences = ctx!!.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(nic, null) != null
//    }
//
//    // This method will give the logged in user
//    fun getUser(): Customer? {
//        val sharedPreferences = ctx!!.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
//        return Customer(
//            sharedPreferences.getString(nic, null).toString(),
//            sharedPreferences.getString(licenseNo, null).toString(),
//            sharedPreferences.getString(name, null).toString(),
//            sharedPreferences.getString(mobNumber, null).toString()
//        )
//    }
//
//    // This method will logout the user
//    fun logout() {
//        val sharedPreferences = ctx!!.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//        ctx!!.startActivity(Intent(ctx, LoginActivity::class.java))
//    }
//}