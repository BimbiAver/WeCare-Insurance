package com.orionsoft.wecareinsurance.model

data class Case(var caseNo: String, var status: String, var vehicleNo: String, var mileage: String, var claimNo: String, var coverNote: String, var debitOut: String, var driverNIC: String, var driverLicenseNo: String, var vehicleTypes: String, var expiryDate: String, var driverName: String, var driverAddress: String, var driverConNumber: String, var accidentDate: String, var accidentTime: String, var location: String, var damage: String, var otherReason: String, var extentDamage: String, var image1: String, var image2: String, var image3: String, var tpVehicleNo: String, var tpVehicleBrandModel: String, var tpDriverName: String, var tpOwnerName: String, var tpAddress: String, var tpDamages: String, var tpOtherDetails: String) {

    constructor(caseNo: String, status: String, vehicleNo: String, claimNo: String, accidentDate: String, accidentTime: String) : this(caseNo, status, vehicleNo, "", claimNo, "", "", "", "", "", "", "", "", "", accidentDate, accidentTime, "", "", "", "", "", "", "", "", "", "", "", "", "", "")

}