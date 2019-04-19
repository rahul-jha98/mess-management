package com.rahul.messmanagement.data.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.NullPointerException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T: Any> Call<T>.await() : NetworkResult<T> {
    return suspendCancellableCoroutine {
        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if(!it.isCancelled){
                    it.resume(NetworkResult.Exception(t))
                }
            }

            override fun onResponse(call: Call<T>, response: Response<T>){
                if(it.isCancelled)
                    return

                if(response.isSuccessful){
                    val body = response.body()
                    if(body == null){
                        it.resume(NetworkResult.Exception(NullPointerException()))
                    }else{
                        it.resume(NetworkResult.Ok(body, response.raw()))
                    }
                }
                else{
                    NetworkResult.Error(HttpException(response), response.raw())
                }
            }

        })
    }
}