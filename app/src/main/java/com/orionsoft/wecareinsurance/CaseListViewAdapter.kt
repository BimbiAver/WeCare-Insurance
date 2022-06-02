package com.orionsoft.wecareinsurance

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.orionsoft.wecareinsurance.model.Case

class CaseListViewAdapter(private val aContext: Context, private val arrayList: ArrayList<Case>): ArrayAdapter<Case>(aContext, R.layout.previous_cases_list_items, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.previous_cases_list_items, null)

        var txtPreCaseNo: TextView = view.findViewById(R.id.txtPreCaseNo)
        var txtPreCaseStatus: TextView = view.findViewById(R.id.txtPreCaseStatus)
        var txtPreCaseDateTime: TextView = view.findViewById(R.id.txtPreCaseDateTime)
        var txtPreCaseVehicleNo: TextView = view.findViewById(R.id.txtPreCaseVehicleNo)
        var txtPreCaseClaimNo: TextView = view.findViewById(R.id.txtPreCaseClaimNo)

        txtPreCaseNo.text = arrayList[position].caseNo
        txtPreCaseStatus.text = arrayList[position].status
        val status = when (arrayList[position].status) {
            "Attended" -> txtPreCaseStatus.setTextColor(Color.parseColor("#0000FF"))
            "Non-Attended" -> txtPreCaseStatus.setTextColor(Color.parseColor("#FFA500"))
            "Completed" -> txtPreCaseStatus.setTextColor(Color.parseColor("#00FF00"))
            "Rejected" -> txtPreCaseStatus.setTextColor(Color.parseColor("#FF0000"))
            else -> ""
        }
        txtPreCaseDateTime.text = arrayList[position].accidentDate + " " + arrayList[position].accidentTime
        txtPreCaseVehicleNo.text = arrayList[position].vehicleNo
        txtPreCaseClaimNo.text = arrayList[position].claimNo

        return view
    }
}