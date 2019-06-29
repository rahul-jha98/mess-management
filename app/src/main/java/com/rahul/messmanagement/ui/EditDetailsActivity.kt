package com.rahul.messmanagement.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_edit_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EditDetailsActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Edit Bank Details"

        dataRepository = (application as MessApplication).appComponent.getRepository()
        holderNameEditText.setText(User.accountHolderName)
        accountNumberEditText.setText(User.accountNo)
        ifscEditText.setText(User.ifscCode)
        bankNameEditText.setText(User.bankName)
        bankBranchEditText.setText(User.bankBranch)

        button.setOnClickListener {
            val name = holderNameEditText.text.toString()
            val accNo = accountNumberEditText.text.toString()
            val ifscCode = ifscEditText.text.toString()
            val bankName = bankNameEditText.text.toString()
            val bankBranch = bankBranchEditText.text.toString()

            if(name.isEmpty()){
                holderNameInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                holderNameInputLayout.error = null
            }

            if(accNo.length < 8) {
                accountNumberInputLayout.error = "Must be at least 8 digits"
                return@setOnClickListener
            } else {
                accountNumberInputLayout.error = null
            }

            if(ifscCode.isEmpty()) {
                ifscInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                ifscInputLayout.error = null
            }

            if(bankName.isEmpty()) {
                bankNameInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                bankNameInputLayout.error = null
            }

            if(bankBranch.isEmpty()) {
                bankBranchInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                bankBranchInputLayout.error = null
            }

            updateDetails(name, accNo, ifscCode, bankName, bankBranch)
        }
    }

    private fun updateDetails(name: String, accNo: String, ifscCode: String, bankName: String, bankBranch: String) {
        launch {
            val result = dataRepository.updateUser(User.rollNo, name, accNo, ifscCode, bankName, bankBranch)

            when(result) {
                is NetworkResult.Ok -> {

                    if(result.value.status) {
                        saveLocally(name, accNo, ifscCode, bankName, bankBranch)
                    }
                }
            }
        }
    }

    private fun saveLocally(name: String, accNo: String, ifscCode: String, bankName: String, bankBranch: String) {
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(getString(com.rahul.messmanagement.R.string.pref_holderName), name)
            putString(getString(com.rahul.messmanagement.R.string.pref_accountNo), accNo)
            putString(getString(com.rahul.messmanagement.R.string.pref_ifscCode), ifscCode)
            putString(getString(com.rahul.messmanagement.R.string.pref_bankBranch), bankBranch)
            putString(getString(com.rahul.messmanagement.R.string.pref_bankName), bankName)
            commit()
        }
        User.accountHolderName = name
        User.accountNo = accNo
        User.ifscCode = ifscCode
        User.bankName = bankName
        User.bankBranch = bankBranch

        runOnUiThread {
            Toast.makeText(this, "Updated Details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

//    private fun saveLocally() {
//        val sharedPref = getSharedPreferences(
//            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putBoolean(getString(com.rahul.messmanagement.R.string.pref_loggedIn), true)
//            putString(getString(com.rahul.messmanagement.R.string.pref_rollNo), user.rollNo)
//            putString(getString(com.rahul.messmanagement.R.string.pref_password), user.password)
//            putString(getString(com.rahul.messmanagement.R.string.pref_email), user.email)
//            putString(getString(com.rahul.messmanagement.R.string.pref_name), user.name)
//            putString(getString(com.rahul.messmanagement.R.string.pref_mess), user.mess)
//            putString(getString(com.rahul.messmanagement.R.string.pref_holderName), user.accountHolderName)
//            putString(getString(com.rahul.messmanagement.R.string.pref_accountNo), user.accountNo)
//            putString(getString(com.rahul.messmanagement.R.string.pref_ifscCode), user.IFSCCode)
//            putString(getString(com.rahul.messmanagement.R.string.pref_bankBranch), user.bankBranch)
//            putString(getString(com.rahul.messmanagement.R.string.pref_bankName), user.bankName)
//            commit()
//        }
//    }

}
