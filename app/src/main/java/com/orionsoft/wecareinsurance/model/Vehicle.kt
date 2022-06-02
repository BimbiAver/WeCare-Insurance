package com.orionsoft.wecareinsurance.model

data class Vehicle(var vehicleNo: String, var type: String, var make: String, var model: String, var manufacYear: String, var transmission: String, var fuel: String, var engCapacity: String, var engNo: String, var chassisNo: String) {

    constructor(vehicleNo: String, make: String, model: String) : this(vehicleNo, "", make, model, "", "", "", "", "", "")
}
