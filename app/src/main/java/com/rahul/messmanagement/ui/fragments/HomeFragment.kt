package com.rahul.messmanagement.ui.fragments


import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.rahul.messmanagement.MessApplication

import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.NetworkResult
import com.rahul.messmanagement.ui.HomeActivity
import com.rahul.messmanagement.ui.listeners.DialogOpenerListener
import com.rahul.messmanagement.utils.User
import com.rahul.messmanagement.viewmodels.MainViewModel
import com.rahul.messmanagement.viewmodels.RoomViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.*
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), CoroutineScope {

    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var dataRepository: DataRepository
    private lateinit var dialogOpenerListener: DialogOpenerListener
    private lateinit var viewModelFactory: RoomViewModelFactory

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var isPresent = 0
    private var timeSlot = 0
    private var slotText = ""
    private var elevation = 0f

    private var ratingTag = 3
    private var isExpanded = false

    private lateinit var mSetRightOut : AnimatorSet
    private lateinit var mSetLeftIn : AnimatorSet


    private var lastButton : Button? = null

    val normalListener = View.OnClickListener {
        badButton.isSelected = true
        veryBadButton.isSelected = true
        goodButton.isSelected = true
        normalButton.isSelected = true
        veryGoodButton.isSelected = true
        it?.isSelected = false
        ratingTag = Integer.parseInt(it?.tag as String)

        lastButton = it as Button

        TransitionManager.beginDelayedTransition(homeFragmentView)
        ratingCardView.isActivated = true
        submitButton.visibility = View.VISIBLE
        complaintEditText.visibility = View.GONE
    }

    val complaintListener = View.OnClickListener {
        badButton.isSelected = true
        veryBadButton.isSelected = true
        goodButton.isSelected = true
        normalButton.isSelected = true
        veryGoodButton.isSelected = true
        it?.isSelected = false

        ratingTag = Integer.parseInt(it?.tag as String)

        lastButton = it as Button

        TransitionManager.beginDelayedTransition(homeFragmentView)
        ratingCardView.isActivated = true
        submitButton.visibility = View.VISIBLE
        complaintEditText.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = (activity?.application as MessApplication).appComponent.getRepository()
        dialogOpenerListener = (activity as HomeActivity)
        viewModelFactory = (activity?.application as MessApplication).appComponent.getViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(User.mess[0] == 'G') {
            Glide.with(this).load(R.drawable.female).into(eventImage)
        }

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)


        Log.d(TAG, hour.toString())
        timeSlot = when(hour) {
            in 0..12 -> 1
            in 12..19 -> 2
            in 19..24 -> 3
            else -> 0
        }

        val viewModel = ViewModelProviders.of(this , viewModelFactory).get(MainViewModel::class.java)

        viewModel.lastEntry.observe(this, androidx.lifecycle.Observer {
            if(it.isNotEmpty()){
                if(it[0].timeSlot == timeSlot && it[0].currDate == TimeUnit.DAYS.toMillis(
                        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()))) {
                    Log.d(TAG, "Already present")
                } else {
                    attendenceCardView.visibility = View.VISIBLE
                    ratingCardView.visibility = View.VISIBLE
                }
            } else {
                attendenceCardView.visibility = View.VISIBLE
                ratingCardView.visibility = View.VISIBLE
            }
        })

        viewModel.nextTimeTable.observe(this, androidx.lifecycle.Observer {
            if(it.isNotEmpty()) {
                slotText = when(timeSlot) {
                    1 -> "breakfast"
                    2 -> "lunch"
                    else -> "dinner"
                }
                menuTimeSlotTextView.text = "Today's ${slotText} Menu"
                menuTextView.text = it[0].menu
            } else {
                nextMenuCardView.visibility = View.GONE
            }
        })
        changeCameraDistance()
        loadAnimations()

        userNameTextView.text = User.name

        if(timeSlot == 0) {
            attendenceCardView.visibility = View.GONE
        } else {
            slotText = when(timeSlot) {
                1 -> "breakfast"
                2 -> "lunch"
                else -> "dinner"
            }

            hadFoodTextView.text = "Did you have $slotText today?"
            howWasFoodTextView.text = "How would you rate the $slotText?"
            hadFoodButton.setOnClickListener {
                isPresent = 1
                flipCard()
                markAttendance()
            }

            didNotHaveFoodButton.setOnClickListener {
                markAttendance()
                TransitionManager.beginDelayedTransition(attendenceCardView)
                ratingCardView.visibility = View.GONE
                attendenceCardView.visibility = View.GONE
            }
        }

        submitButton.setOnClickListener {
            ratingCardView.isActivated = false
            TransitionManager.beginDelayedTransition(ratingCardView)
            howWasFoodTextView.visibility = View.GONE
            complaintEditText.visibility = View.GONE
            submitButton.visibility = View.GONE
            giveRating()
        }

        rebateCardView.setOnClickListener {
            dialogOpenerListener.openDialog(1)
        }
        applyButton.setOnClickListener {
            dialogOpenerListener.openDialog(1)
        }

    }

    private fun markAttendance() {
        val msecs = TimeUnit.DAYS.toMillis(
            TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()))

        if(timeSlot == 0) return

        launch {
            val result = dataRepository.markAttendance(User.rollNo, msecs, timeSlot, isPresent)

            when(result) {
                is NetworkResult.Ok -> {
                    Log.d(TAG, result.value.status.toString())

                    if(result.value.status) {
                        dataRepository.saveAttendance(User.rollNo, msecs, timeSlot, isPresent)
                    }
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, result.exception.toString())
                }
                is NetworkResult.Exception -> {
                    Log.d(TAG, result.exception.toString())
                }
            }
        }
    }

    private fun giveRating() {
        val msecs = TimeUnit.DAYS.toMillis(
            TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()))

        if(timeSlot == 0) return

        launch {
            val result = dataRepository.giveRating(User.rollNo, msecs, timeSlot, ratingTag, complaintEditText.text.toString())

            when(result) {
                is NetworkResult.Ok -> {
                    Log.d(TAG, result.value.status.toString())

                    if(result.value.status) {
                        activity?.runOnUiThread {
                            Snackbar.make(attendenceCardView, "Your rating has been submitted", Snackbar.LENGTH_SHORT).show()
                            ratingCardView.visibility = View.GONE
                        }
                    } else {
                        activity?.runOnUiThread {
                            Snackbar.make(attendenceCardView, "User has already rated", Snackbar.LENGTH_SHORT).show()
                            ratingCardView.visibility = View.GONE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, result.exception.toString())
                    activity?.runOnUiThread {
                        ratingCardView.isActivated = true
                        TransitionManager.beginDelayedTransition(ratingCardView)
                        howWasFoodTextView.visibility = View.VISIBLE
                        complaintEditText.visibility = View.VISIBLE
                        submitButton.visibility = View.VISIBLE
                    }
                }
                is NetworkResult.Exception -> {
                    Log.d(TAG, result.exception.toString())
                }
            }
        }
    }

    private fun flipCard() {
        attendenceCardView.cardElevation = 0f
        mSetRightOut.setTarget(attendenceCardView)
        mSetLeftIn.setTarget(ratingCardView)

        mSetLeftIn.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                attendenceCardView.visibility = View.GONE
                ratingCardView.elevation = elevation

                veryBadButton.setOnClickListener(complaintListener)
                veryGoodButton.setOnClickListener(normalListener)
                goodButton.setOnClickListener(normalListener)
                badButton.setOnClickListener(complaintListener)
                normalButton.setOnClickListener(normalListener)
            }
        })

        mSetRightOut.start()
        mSetLeftIn.start()


    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        elevation = resources.displayMetrics.density * 2
        attendenceCardView.cameraDistance = scale
        ratingCardView.cameraDistance = scale
    }

    private fun loadAnimations() {
        mSetRightOut = AnimatorInflater.loadAnimator(context, R.animator.out_anim) as AnimatorSet
        mSetLeftIn = AnimatorInflater.loadAnimator(context, R.animator.in_anim) as AnimatorSet
    }

}
