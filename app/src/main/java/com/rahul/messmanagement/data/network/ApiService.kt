package com.rahul.messmanagement.data.network

import com.rahul.messmanagement.data.database.AttendanceEntry
import com.rahul.messmanagement.data.database.MenuEntry
import com.rahul.messmanagement.data.network.networkmodels.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("getRebates")
    fun getAllRebates(@Query("rollNo") rollNo : String) : Call<List<RebateRequest>>

    @PUT("user")
    fun updateUser(@Body bankDetails: BankDetails) : Call<StatusPostResponse>

    @GET("attendance/{rollNo}")
    fun getAttendance(@Path("rollNo") rollNo: String) : Call<List<AttendanceEntry>>

    @GET("timetable/{mess}")
    fun getTimeTable(@Path("mess") mess: String) : Call<List<MenuEntry>>

    @GET("polls/{mess}")
    fun getPolls(@Path("mess") mess: String) : Call<List<PollQuestion>>

    @GET("poll/options/{id}")
    fun getPollOptions(@Path("id") id: Int) : Call<List<PollOptions>>

    @POST("poll/response")
    fun submitPollResponse(@Body pollResponse: PollResponse) : Call<StatusPostResponse>
}