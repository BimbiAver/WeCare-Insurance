package com.orionsoft.wecareinsurance.model

data class Policy(var policyNo: String, var coverPeriod: String, var sumInsured: String, var type: String,) {

    constructor(policyNo: String) : this(policyNo, "", "", "")
}
