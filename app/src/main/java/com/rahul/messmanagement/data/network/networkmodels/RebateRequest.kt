package com.rahul.messmanagement.data.network.networkmodels

class RebateRequest (
    var rollNo: String,
    var fromDate: Long,
    var toDate: Long,
    var status : Int = 0
)