package com.orionsoft.wecareinsurance

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.orionsoft.wecareinsurance.model.Policy
import com.orionsoft.wecareinsurance.model.Vehicle

class VehicleListViewAdapter(private val aContext: Context, private val vehicleArrayList: ArrayList<Vehicle>, private val policyArrayList: ArrayList<Policy>): ArrayAdapter<Vehicle>(aContext, R.layout.vehicle_list_items, vehicleArrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.vehicle_list_items, null)

        var txtRegVehNo: TextView = view.findViewById(R.id.txtRegVehNo)
        var txtRegVehMake: TextView = view.findViewById(R.id.txtRegVehMake)
        var txtRegVehModel: TextView = view.findViewById(R.id.txtRegVehModel)
        var txtRegVehPolicyNo: TextView = view.findViewById(R.id.txtRegVehPolicyNo)

        txtRegVehNo.text = vehicleArrayList[position].vehicleNo
        txtRegVehMake.text = vehicleArrayList[position].make
        txtRegVehModel.text = vehicleArrayList[position].model
        txtRegVehPolicyNo.text = policyArrayList[position].policyNo

        return view
    }
}