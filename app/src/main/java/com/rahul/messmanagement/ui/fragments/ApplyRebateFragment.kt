package com.rahul.messmanagement.ui.fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.rahul.messmanagement.MessApplication

import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.ui.registration.listeners.LoginInterfaceListener
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_apply_rebate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class ApplyRebateFragment : BottomSheetDialogFragment(), CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository

    private var fromDate = 0L
    private var toDate = 0L

    private var fromYear = 1998
    private var fromMonth = 1
    private var fromDay = 1

    private var toYear = 1998
    private var toMonth = 1
    private var toDay = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_rebate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountNoTextView.text = User.accountNo
        ifscCodeTextView.text = User.ifscCode
        bankNameTextView.text = User.bankName
        bankBranchTextView.text = User.bankBranch

        fromEditText.showSoftInputOnFocus = false
        toEditText.showSoftInputOnFocus = false

        fromDate = System.currentTimeMillis()

        val fromPickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromEditText.setText("$dayOfMonth - ${month+1} - $year")
            fromDay = dayOfMonth
            fromMonth = month
            fromYear = year

            val instance = Calendar.getInstance()
            instance.set(year, month, dayOfMonth, 0, 0)
            fromDate = instance.timeInMillis
        }

        val toPickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            toEditText.setText("$dayOfMonth - ${month+1} - $year")
            toDay = dayOfMonth
            toMonth = month
            toYear = year

            val instance = Calendar.getInstance()
            instance.set(year, month, dayOfMonth, 0, 0)
            toDate = instance.timeInMillis
        }

        fromEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                val fromDialog = DatePickerDialog(context!!, fromPickerListener, fromYear, fromMonth, fromDay)
                fromDialog.datePicker.minDate = System.currentTimeMillis()
                fromDialog.show()

                fromEditText.clearFocus()
            }
        }
//
        toEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                val toDialog = DatePickerDialog(context!!, toPickerListener, toYear, toMonth, toDay)
                toDialog.datePicker.minDate = when(fromDate) {
                    0L ->System.currentTimeMillis()
                    else -> fromDate + 345600000
                }
                toDialog.show()

                toEditText.clearFocus()
            }
        }

        continueButton.setOnClickListener {
            if(fromEditText.text.toString().isEmpty()) {
                fromInputLayout.error = "Should not be empty"
                return@setOnClickListener
            } else {
                fromInputLayout.error = null
            }

            if(toEditText.text.toString().isEmpty()) {
                toInputLayout.error = "Should not be empty"
                return@setOnClickListener
            } else if (toDate - fromDate < 345600000) {
                toInputLayout.error = "Gap less than 4 days"
                return@setOnClickListener
            } else {
                toInputLayout.error = null
            }

            hideButton()
            applyForRebate()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = (activity?.application as MessApplication).appComponent.getRepository()
    }

    private fun applyForRebate() {

        fromDate = TimeUnit.DAYS.toMillis(
            TimeUnit.MILLISECONDS.toDays(fromDate))
        toDate = TimeUnit.DAYS.toMillis(
            TimeUnit.MILLISECONDS.toDays(toDate))
        launch {
            val result = dataRepository.applyForRebate(User.rollNo, fromDate, toDate)
            when(result) {
                is NetworkResult.Ok -> {

                    val message = when(result.value.status) {
                        true -> "Rebate Application Submitted"
                        false -> "Same application submitted before"
                    }
                    activity?.runOnUiThread{
                        dismiss()
                        Snackbar.make(activity!!.rebateCardView, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    dismiss()
                }
                is NetworkResult.Exception -> {
                    dismiss()
                    Snackbar.make(activity!!.rebateCardView, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideButton() {
        val cx = continueButton.width / 2
        val cy = continueButton.height / 2

        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val animator = ViewAnimationUtils.createCircularReveal(continueButton, cx, cy, finalRadius, 0f)
        animator.duration = 250L

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                continueButton.visibility = View.INVISIBLE
                continueProgressBar.visibility = View.VISIBLE
            }
        })

        animator.start()
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
