package com.rahul.messmanagement.data.network.networkmodels

class RatingRequest(var rollNo : String = "",
                        var currDate : Long = 0L,
                        var timeSlot : Int = 0,
                        var rating: Int = 0,
                        var complaint: String?)