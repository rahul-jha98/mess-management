package com.rahul.messmanagement.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.adapters.RebateAdapter
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RebateActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository
    private lateinit var rebateAdapter: RebateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rebate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Your Rebate Applications"

        dataRepository = (application as MessApplication).appComponent.getRepository()

        rebateAdapter = RebateAdapter(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = rebateAdapter
        }

        launch {
            val result = dataRepository.getAllRebates(User.rollNo)
            when(result) {
                is NetworkResult.Ok -> {
                    runOnUiThread {

                        if(result.value.isNullOrEmpty()) {
                            progressBar.visibility = View.GONE
                            emptyImageView.visibility = View.VISIBLE
                            emptyTextView.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE

                            rebateAdapter.swapList(result.value)
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
}
