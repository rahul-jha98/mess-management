package com.rahul.messmanagement.ui.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.rahul.messmanagement.R
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.fragment_apply_rebate.*
import java.util.*

class ApplyRebateFragment : BottomSheetDialogFragment() {

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

        val fromView = view.findViewById<EditText>(R.id.fromEditText)

        fromDate = System.currentTimeMillis()

        val fromPickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromEditText.setText("$dayOfMonth - $month - $year")
            fromDay = dayOfMonth
            fromMonth = month
            fromYear = year

            val instance = Calendar.getInstance()
            instance.set(year, month, dayOfMonth, 0, 0)
            fromDate = instance.timeInMillis
        }

        val toPickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            toEditText.setText("$dayOfMonth - $month - $year")
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
                    else -> fromDate
                }
                toDialog.show()

                toEditText.clearFocus()
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
