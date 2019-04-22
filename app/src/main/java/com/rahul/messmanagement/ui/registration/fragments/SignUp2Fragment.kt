package com.rahul.messmanagement.ui.registration.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.rahul.messmanagement.R
import com.rahul.messmanagement.ui.registration.MainActivity
import kotlinx.android.synthetic.main.fragment_sign_up2.*


class SignUp2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var email = MainActivity.rollNo.toLowerCase() + "@iiita.ac.in"
        emailEditText.setText(email)

        button.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val mess = messEditText.text.toString()

            if(name.isEmpty()){
                nameInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                nameInputLayout.error = null
            }

            if(email.isEmpty()) {
                emailInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                emailInputLayout.error = null
            }

            if(mess.isEmpty()) {
                messInputLayout.error = "Should not be blank"
                return@setOnClickListener
            } else {
                messInputLayout.error = null
            }

            MainActivity.name = name
            MainActivity.email = email
            MainActivity.mess = mess

            SignUpHandlerFragment.viewPager.currentItem = 2
        }
    }
}
