package com.rahul.messmanagement.data

import android.util.Log
import com.rahul.messmanagement.AppExecutors
import com.rahul.messmanagement.data.network.ApiService
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.data.network.await
import com.rahul.messmanagement.data.network.networkmodels.*
import com.rahul.messmanagement.ui.registration.MainActivity

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

    suspend fun signUp() : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Signing Up")
        return retorfitClient.signUp(User(
            MainActivity.rollNo,
            MainActivity.password,
            MainActivity.email,
            MainActivity.name,
            0,
            MainActivity.mess,
            MainActivity.accountNo,
            MainActivity.ifscCode,
            MainActivity.bankName,
            MainActivity.bankBranch,
            MainActivity.accountHolderName)).await()
    }

    suspend fun loginGet(rollNo: String) : NetworkResult<User> {
        Log.d(LOG_TAG, "Getting details")
        return retorfitClient.loginGet(rollNo).await()
    }

    suspend fun markAttendance(rollNo: String, midnightTimeInMillis : Long, timeSlot: Int) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Marking Attendance")
        return retorfitClient.markAttendance(AttendanceRequest(rollNo, midnightTimeInMillis, timeSlot)).await()
    }

    suspend fun giveRating(rollNo: String, midnightTimeInMillis : Long, timeSlot: Int,
                           rating: Int, complaint: String) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Marking Attendance")
        return retorfitClient.submitFeedback(RatingRequest(rollNo, midnightTimeInMillis, timeSlot, rating, complaint)).await()
    }
}