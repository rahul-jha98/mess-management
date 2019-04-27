package com.rahul.messmanagement.data.network

import com.rahul.messmanagement.data.database.AttendanceEntry
import com.rahul.messmanagement.data.network.networkmodels.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("user/checkAvailable")
    fun checkAvailable(@Query("rollNo") rollNo: String) : Call<StatusPostResponse>

    @POST("login")
    fun loginPost(@Body userDetails : UserLoginRequest) : Call<StatusPostResponse>

    @GET("user?")
    fun loginGet(@Query("rollNo") rollNo : String) : Call<User>

    @POST("user")
    fun signUp(@Body userDetails: User) : Call<StatusPostResponse>

    @POST("attendance")
    fun markAttendance(@Body attendanceRequest: AttendanceEntry) : Call<StatusPostResponse>

    @POST("feedback")
    fun submitFeedback(@Body ratingRequest: RatingRequest) : Call<StatusPostResponse>

    @POST("apply_rebate")
    fun applyForRebate(@Body rebateRequest: RebateRequest) : Call<StatusPostResponse>

    @GET("getFeedbacks")
    fun getAllFeedbacks(@Query("rollNo") rollNo: String) : Call<List<RatingRequest>>
}