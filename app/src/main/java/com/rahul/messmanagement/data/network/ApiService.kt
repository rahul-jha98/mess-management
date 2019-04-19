package com.rahul.messmanagement.data.network

import com.rahul.messmanagement.data.network.networkmodels.StatusPostResponse
import com.rahul.messmanagement.data.network.networkmodels.User
import com.rahul.messmanagement.data.network.networkmodels.UserLoginRequest
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
}