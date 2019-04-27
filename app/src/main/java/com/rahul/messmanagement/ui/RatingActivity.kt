package com.rahul.messmanagement.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.adapters.RatingAdapter
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.data.network.networkmodels.RatingRequest
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class RatingActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository
    private lateinit var ratingAdapter: RatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Your Feedbacks"

        dataRepository = (application as MessApplication).appComponent.getRepository()

        ratingAdapter = RatingAdapter(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = ratingAdapter
        }

        launch {
            val result = dataRepository.getAllFeedbacks(User.rollNo)
            when(result) {
                is NetworkResult.Ok -> {
                    Log.d("Success", result.value[0].complaint.toString())
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                        ratingAdapter.swapList(result.value)
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
