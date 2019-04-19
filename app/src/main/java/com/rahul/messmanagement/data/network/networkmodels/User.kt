package com.rahul.messmanagement.data.network.networkmodels

class User(
    val rollNo : String,
    val password: String,
    val email : String,
    val name : String,
    val verified : Long,
    val mess : String,
    val accountNo : String,
    val IFSCCode : String,
    val bankName : String,
    val bankBranch : String,
    val accountHolderName : String
)