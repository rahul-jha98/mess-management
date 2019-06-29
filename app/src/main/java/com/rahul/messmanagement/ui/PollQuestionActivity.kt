package com.rahul.messmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.adapters.PollAdapter
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_poll_question.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PollQuestionActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var dataRepository: DataRepository
    private lateinit var pollAdapter: PollAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll_question)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "All Polls"

        dataRepository = (application as MessApplication).appComponent.getRepository()

        pollAdapter = PollAdapter(this) {
            val intent = Intent(this, PollActivity::class.java)
            intent.putExtra("poll", it)
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = pollAdapter
        }

        launch {
            val result = dataRepository.getAllPolls(User.mess)
            when (result) {
                is NetworkResult.Ok -> {
                    runOnUiThread {

                        if (result.value.isNullOrEmpty()) {
                            progressBar.visibility = View.GONE
                            emptyImageView.visibility = View.VISIBLE
                            emptyTextView.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE

                            pollAdapter.swapList(result.value)
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
