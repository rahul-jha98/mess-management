package com.rahul.messmanagement.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.rahul.messmanagement.AppExecutors
import com.rahul.messmanagement.data.database.AttendanceDao
import com.rahul.messmanagement.data.database.AttendanceEntry
import com.rahul.messmanagement.data.database.MenuDao
import com.rahul.messmanagement.data.database.MenuEntry
import com.rahul.messmanagement.data.network.ApiService
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.data.network.await
import com.rahul.messmanagement.data.network.networkmodels.*
import com.rahul.messmanagement.ui.registration.MainActivity
import java.time.DayOfWeek

class DataRepository(private var retorfitClient : ApiService,
                     private var mAttendanceDao: AttendanceDao,
                     private var mMenuDao: MenuDao,
                     private var mExecutors : AppExecutors
) {
    companion object {
        private val LOG_TAG = DataRepository::class.java.simpleName

        private val LOCK = Object()
        private var sInstance : DataRepository? = null
        private var mInitialized = false

        @Synchronized
        fun getInstance( retrofitClient : ApiService,
                         attendanceDao: AttendanceDao,
                         menuDao: MenuDao,
                         executors: AppExecutors) : DataRepository{
            Log.d(LOG_TAG, "Getting the repository")
            if(sInstance == null) {
                synchronized(LOCK) {
                    sInstance = DataRepository(retrofitClient, attendanceDao, menuDao, executors)
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

    suspend fun markAttendance(rollNo: String, midnightTimeInMillis : Long, timeSlot: Int, isPresent: Int) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Marking Attendance")
        return retorfitClient.markAttendance(AttendanceEntry(rollNo, midnightTimeInMillis, timeSlot, isPresent)).await()
    }

    suspend fun giveRating(rollNo: String, midnightTimeInMillis : Long, timeSlot: Int,
                           rating: Int, complaint: String) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Marking Attendance")
        return retorfitClient.submitFeedback(RatingRequest(rollNo, midnightTimeInMillis, timeSlot, rating, complaint)).await()
    }

    suspend fun applyForRebate(rollNo: String, fromDate: Long, toDate: Long) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Applying for Rebate")
        return retorfitClient.applyForRebate(RebateRequest(rollNo, fromDate, toDate)).await()
    }

    suspend fun getAttendanceFromServer(rollNo: String) : NetworkResult<List<AttendanceEntry>> {
        return retorfitClient.getAttendance(rollNo).await()
    }

    suspend fun getMenuFromServer(mess: String) : NetworkResult<List<MenuEntry>> {
        return retorfitClient.getTimeTable(mess).await()
    }

    @Synchronized
    fun saveAttendance(rollNo: String, midnightTimeInMillis: Long, timeSlot: Int, isPresent: Int) {
        Log.d(LOG_TAG, "Saving attendance to database")
        mExecutors.diskIO().execute {
            mAttendanceDao.markAttendance(AttendanceEntry(rollNo, midnightTimeInMillis, timeSlot, isPresent))
        }
    }

    fun getAllAttendance() : LiveData<List<AttendanceEntry>> {
        return mAttendanceDao.getAllAttendance()
    }

    suspend fun getAllFeedbacks(rollNo: String) : NetworkResult<List<RatingRequest>> {
        return retorfitClient.getAllFeedbacks(rollNo).await()
    }

    suspend fun getAllRebates(rollNo: String) : NetworkResult<List<RebateRequest>> {
        return retorfitClient.getAllRebates(rollNo).await()
    }

    suspend fun updateUser(rollNo: String, holderName: String, accountNumber: String, ifscCode: String, bankName: String, bankBranch: String) : NetworkResult<StatusPostResponse> {
        Log.d(LOG_TAG, "Updating User")
        return retorfitClient.updateUser(BankDetails(rollNo, accountNumber, ifscCode, bankName, bankBranch, holderName)).await()
    }

    fun getLastEntry() : LiveData<List<AttendanceEntry>> {
        return mAttendanceDao.getLastAttendance()
    }

    fun deleteAll() {
        mExecutors.diskIO().execute {
            mAttendanceDao.deleteAll()
            mMenuDao.deleteAll()
        }
    }

    @Synchronized
    fun saveAllAttendance(list: List<AttendanceEntry>) {
        mExecutors.diskIO().execute {
            mAttendanceDao.saveAttendance(list)
        }
    }

    @Synchronized
    fun saveAllMenu(list: List<MenuEntry>) {
        mExecutors.diskIO().execute {
            mMenuDao.saveMenu(list)
        }
    }

    fun getMenuForDay(day : Int) : LiveData<List<MenuEntry>>{
        return mMenuDao.getMenuForDay(day)
    }

    fun getAllMenu(timeSlot: Int, dayOfWeek: Int): LiveData<List<MenuEntry>> {
        return mMenuDao.getNextMenu(dayOfWeek, timeSlot)
    }

    suspend fun getAllPolls(mess: String) : NetworkResult<List<PollQuestion>> {
        return retorfitClient.getPolls(mess).await()
    }

    suspend fun getOptions(id : Int) : NetworkResult<List<PollOptions>> {
        return retorfitClient.getPollOptions(id).await()
    }

    suspend fun submitPostResponse(rollNo: String, pollInt: Int, optNo : Int) : NetworkResult<StatusPostResponse>{
        return retorfitClient.submitPollResponse(PollResponse(rollNo, pollInt, optNo)).await()
    }
}