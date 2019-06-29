package com.rahul.messmanagement.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.data.network.networkmodels.PollOptions
import com.rahul.messmanagement.data.network.networkmodels.PollQuestion
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_poll.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PollActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()
    private var allOptionsList = ArrayList<PollOptions>()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll)

        val poll = intent.getParcelableExtra<PollQuestion>("poll")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "All Polls"

        dataRepository = (application as MessApplication).appComponent.getRepository()

        questionTv.text = poll.question

        optionEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                optionSpinnerView.performClick()
            }
        }

        optionEditText.showSoftInputOnFocus = false

        optionSpinnerView.onAnyItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                optionEditText.clearFocus()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                optionEditText.setText(allOptionsList?.get(position)?.optionText)
                optionEditText.clearFocus()
            }
        }

        launch {
            val result = dataRepository.getOptions(poll.id)
            when (result) {
                is NetworkResult.Ok -> {
                    runOnUiThread {
                        optionSpinnerView.adapter = SubjectSpinnerAdapter(this@PollActivity, result.value)
                        allOptionsList.addAll(result.value)
                    }
                }
            }
        }

        button.setOnClickListener {
            val option = optionEditText.text.toString()

            if(option.isEmpty()) {
                optionInputLayout.error = "One option should be selected"
                return@setOnClickListener
            } else {
                optionInputLayout.error = null
            }
            val optionNumber = optionSpinnerView.selectedItem as PollOptions
            launch {
                val result = dataRepository.submitPostResponse(User.rollNo, optionNumber.pollId, optionNumber.optNo)
                when(result) {
                    is NetworkResult.Ok -> {
                        if(result.value.status) {
                            runOnUiThread {
                                Toast.makeText(this@PollActivity, "Response Submitted", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@PollActivity, "Already submitted response", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when(item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                } else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class SubjectSpinnerAdapter(private var mContext: Context, private val list : List<PollOptions>) : ArrayAdapter<PollOptions>(mContext, R.layout.list_item_spinner_drop_down, list) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.list_item_spinner_drop_down,parent, false)
            if(view is TextView) {
                view.text = list[position].optionText
            }
            return view
        }
    }
}
