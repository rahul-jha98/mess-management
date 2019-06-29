package com.rahul.messmanagement.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.rahul.messmanagement.R
import com.rahul.messmanagement.ui.fragments.ApplyRebateFragment
import com.rahul.messmanagement.ui.fragments.HomeFragment
import com.rahul.messmanagement.ui.fragments.MenuFragment
import com.rahul.messmanagement.ui.fragments.UserDetailsFragment
import com.rahul.messmanagement.ui.listeners.DialogOpenerListener
import com.rahul.messmanagement.utils.User
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity(), DialogOpenerListener {
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchToFragment(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_food -> {
                switchToFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user_detail-> {
                switchToFragment(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val cal = Calendar.getInstance()
        val hourOfDay = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        User.dayOfWeek = when(dayOfWeek) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 6
        }

        User.timeSlot = when(hourOfDay) {
            in 0..9 -> 1
            in 10..15 -> 2
            else -> 3
        }

        floatingActionButton.hide()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        User.name = sharedPref.getString(getString(R.string.pref_name), "")
        User.password = sharedPref.getString(getString(R.string.pref_password), "")
        User.email = sharedPref.getString(getString(R.string.pref_email), "")
        User.rollNo = sharedPref.getString(getString(R.string.pref_rollNo), "")
        User.mess = sharedPref.getString(getString(R.string.pref_mess), "")

        User.bankName = sharedPref.getString(getString(R.string.pref_bankName), "")
        User.bankBranch = sharedPref.getString(getString(R.string.pref_bankBranch), "")
        User.accountNo = sharedPref.getString(getString(R.string.pref_accountNo), "")
        User.accountHolderName = sharedPref.getString(getString(R.string.pref_holderName), "")
        User.ifscCode =  sharedPref.getString(getString(R.string.pref_ifscCode), "")

        supportFragmentManager.beginTransaction().replace(R.id.homeContentSpace, HomeFragment()).commit()
    }

    private fun switchToFragment(fragmentNo: Int) {
        val fragment = when(fragmentNo) {
            0 -> HomeFragment()
            1 -> MenuFragment()
            else -> UserDetailsFragment()
        }

        if(fragmentNo == 2) {
            floatingActionButton.show()
            floatingActionButton.setOnClickListener {
                startActivity(Intent(this, EditDetailsActivity::class.java))
            }
        } else {
            floatingActionButton.hide()
        }
        supportFragmentManager.beginTransaction().replace(R.id.homeContentSpace, fragment).commit()
    }

    override fun openDialog(dialogNo: Int) {
        val applyRebateFragment = ApplyRebateFragment()
        applyRebateFragment.show(supportFragmentManager, applyRebateFragment.tag)
    }
}
