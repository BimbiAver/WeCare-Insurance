package com.orionsoft.wecareinsurance

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.orionsoft.wecareinsurance.model.Vehicle

class VehicleListViewAdapter(private val aContext: Context, private val arrayList: ArrayList<Vehicle>): ArrayAdapter<Vehicle>(aContext, R.layout.vehicle_list_items, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.vehicle_list_items, null)

        var txtRegVehNo: TextView = view.findViewById(R.id.txtRegVehNo)
        var txtRegVehMake: TextView = view.findViewById(R.id.txtRegVehMake)
        var txtRegVehModel: TextView = view.findViewById(R.id.txtRegVehModel)

        txtRegVehNo.text = arrayList[position].vehicleNo
        txtRegVehMake.text = arrayList[position].make
        txtRegVehModel.text = arrayList[position].model

        return view
    }
}