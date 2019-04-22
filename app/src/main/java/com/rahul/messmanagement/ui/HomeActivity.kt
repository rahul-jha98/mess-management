package com.rahul.messmanagement.ui

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.rahul.messmanagement.R
import com.rahul.messmanagement.ui.fragments.HomeFragment
import com.rahul.messmanagement.ui.fragments.UserDetailsFragment
import com.rahul.messmanagement.utils.User

class HomeActivity : AppCompatActivity() {
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
            in 0..1 -> HomeFragment()
            else -> UserDetailsFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.homeContentSpace, fragment).commit()
    }
}
