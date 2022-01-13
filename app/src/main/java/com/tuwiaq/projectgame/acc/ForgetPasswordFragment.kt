package com.tuwiaq.projectgame.acc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.model.MainViewModel

class ForgetPasswordFragment : Fragment() {
    private lateinit var submit: Button
    private lateinit var emailE: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private val acc_forget: MainViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forget_password, container, false)
        submit = view.findViewById(R.id.submit1)
        emailE = view.findViewById(R.id.f_emailBtn)
        firebaseAuth = FirebaseAuth.getInstance()

        submit.setOnClickListener {
            val email = emailE.text.toString().trim() { it <= ' ' }
            if (email.isEmpty()) {
                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()

            } else {

                acc_forget.resetUserPassword(email,requireView())
            }
        }

        return view
    }

}