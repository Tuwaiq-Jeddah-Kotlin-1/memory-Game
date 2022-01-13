package com.tuwiaq.projectgame.acc

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.model.MainViewModel

class LogInFragment : Fragment() {
    private lateinit var progressBar: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var login: Button
    private lateinit var forgetPass: Button
    private lateinit var noAcc: TextView
    private lateinit var ema: TextInputLayout
    private lateinit var pass: TextInputLayout
    private var email = ""
    private var password = ""
    private val acc_logIn:MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)
        login = view.findViewById(R.id.loginBtn)
        noAcc = view.findViewById(R.id.noAccTv)
        ema = view.findViewById(R.id.email)
        pass = view.findViewById(R.id.password_til)
       forgetPass = view.findViewById(R.id.forget)

        progressBar = ProgressDialog(context)
        progressBar.setTitle(getString(R.string.Please_wait))
        progressBar.setMessage(getString(R.string.Logging_in))
        progressBar.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        noAcc.setOnClickListener {
           it.findNavController().navigate(R.id.signInFragment)

            refreshCurrentFragment()

        }
         forgetPass.setOnClickListener {
             findNavController().navigate(R.id.forgetPasswordFragment)
         }

        login.setOnClickListener {
             validDate()
            Log.e("aaaa","this islogin")

        }

        return view
    }


    private fun validDate() {
            email = ema.editText!!.text.toString().trim()
          password = pass.editText!!.text.toString()
        if (email.isEmpty()||password.isEmpty()){

            Toast.makeText(requireContext(),"Invalid email or password format",Toast.LENGTH_SHORT).show()
        }else{
            acc_logIn.signIn(email, password,requireView())
        }

     }


    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }


}