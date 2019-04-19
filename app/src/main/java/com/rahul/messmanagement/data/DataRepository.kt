package com.rahul.messmanagement.data

import android.util.Log
import com.rahul.messmanagement.AppExecutors
import com.rahul.messmanagement.data.network.ApiService
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.data.network.await
import com.rahul.messmanagement.data.network.networkmodels.StatusPostResponse
import com.rahul.messmanagement.data.network.networkmodels.UserLoginRequest

class DataRepository(private var retorfitClient : ApiService,
                     private var mExecutors : AppExecutors
) {
    companion object {
        private val LOG_TAG = DataRepository::class.java.simpleName

        private val LOCK = Object()
        private var sInstance : DataRepository? = null
        private var mInitialized = false

        @Synchronized
        fun getInstance( retrofitClient : ApiService,
                         executors: AppExecutors) : DataRepository{
            Log.d(LOG_TAG, "Getting the repository")
            if(sInstance == null) {
                synchronized(LOCK) {
                    sInstance = DataRepository(retrofitClient,executors)
                    Log.d(LOG_TAG, "Made a new repository")
                }
            }
            return sInstance!!
        }
    }



    suspend fun checkRegistrationStatus(rollNo : String) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Checking registration status")
        return retorfitClient.checkAvailable(rollNo).await()
    }

    suspend fun login(rollNo: String, password: String) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Logging in")
        return retorfitClient.loginPost(UserLoginRequest(rollNo, password)).await()
    }
}