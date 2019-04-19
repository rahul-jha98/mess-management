package com.rahul.messmanagement.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.transition.Slide
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import com.rahul.messmanagement.R
import com.rahul.messmanagement.ui.fragments.LoginFragment
import com.rahul.messmanagement.ui.fragments.RollNoFragment
import com.rahul.messmanagement.ui.listeners.LoginInterfaceListener
import kotlinx.android.synthetic.main.fragment_roll_no.*


class MainActivity : AppCompatActivity(), LoginInterfaceListener {

    companion object {
        var rollNo = ""
    }

    private val TAG = MainActivity::class.java.simpleName

    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(fragment == null) {
            try {
                fragment = RollNoFragment() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.mainContentView, fragment!!).commit()

    }

    override fun switchToFragment(fragmentNo: Int) {
        val nextFragment = LoginFragment()



        val slide = Fade()
//        slide.slideEdge = Gravity.BOTTOM
        nextFragment.enterTransition = slide

        supportFragmentManager.beginTransaction().replace(R.id.mainContentView, nextFragment).commit()
    }
}
