package com.rahul.messmanagement.ui.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rahul.messmanagement.MessApplication

import com.rahul.messmanagement.R
import com.rahul.messmanagement.ui.*
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_user_details.*

class UserDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(User.mess[0] == 'G') {
            Glide.with(this).load(R.drawable.female).into(imageView6)
        }

        rollNoTextView.text = User.rollNo
        nameTextView.text = User.name
        emailTextView.text = User.email
        messTextView.text = User.mess

        signOutButton.setOnClickListener {
            val sharedPref = activity!!.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putBoolean(getString(com.rahul.messmanagement.R.string.pref_loggedIn), false)
                putBoolean(getString(com.rahul.messmanagement.R.string.pref_Verified), false)
                commit()
            }

            activity!!.finish()
        }

        attendenceButton.setOnClickListener {
            startActivity(Intent(activity!!, AttendanceActivity::class.java))
        }

        yourReviewsButton.setOnClickListener {
            startActivity(Intent(activity!!, RatingActivity::class.java))
        }

        viewRebateButton.setOnClickListener {
            startActivity(Intent(activity!!, RebateActivity::class.java))
        }

        allPollsButton.setOnClickListener {
            startActivity(Intent(activity!!, PollQuestionActivity::class.java))
        }
    }
}
