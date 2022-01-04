package com.tuwiaq.projectgame.acc

import android.app.ProgressDialog
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
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
        // Inflate the layout for this fragment
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
            //firebaseLogin()
           // progressBar.show()
            acc_logIn.signIn(email, password,requireView())
        }

  /*      if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ema.error = getString(R.string.Invalid_email_format)
        }else if (TextUtils.isEmpty(password)){
            pass.error = getString(R.string.Please_enter_password)
        }else{
            acc_logIn.signIn(email, password,requireView())
            Log.e("eee","this islogin")

        }*/

     }


    private fun firebaseLogin() {
        progressBar.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressBar.dismiss()
                val firebaseUser = acc_logIn.signIn(email,password,requireView())
                val email = firebaseUser!!

                Log.e("ttt","this islogin")

                Toast.makeText(requireContext(),"Loggin as $email",Toast.LENGTH_SHORT).show()
                //startActivity intent to profile

            }.addOnFailureListener {
                progressBar.dismiss()
                Log.e("hhh","this islogin")
                Toast.makeText(requireContext(),"Log in failed ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }


}