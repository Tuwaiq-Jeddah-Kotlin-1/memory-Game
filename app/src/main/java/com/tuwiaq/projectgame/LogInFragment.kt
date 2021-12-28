package com.tuwiaq.projectgame

import android.app.ActionBar
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.util.regex.Pattern

class LogInFragment : Fragment() {
    private lateinit var progressBar: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var login: Button
    private lateinit var noAcc: TextView
    private lateinit var ema: TextInputLayout
    private lateinit var pass: TextInputLayout
    private var email = ""
    private var password = ""
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

        progressBar = ProgressDialog(context)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Logging in...")
        progressBar.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        noAcc.setOnClickListener {
           it.findNavController().navigate(R.id.signInFragment)

            refreshCurrentFragment()

        }

        login.setOnClickListener {
             validDate()

            findNavController().navigate(R.id.mainFragment)
        }
        return view
    }

    private fun validDate() {
            email = ema.toString().trim()
          password = pass.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ema.error = "Invalid email format"
        }else if (TextUtils.isEmpty(password)){
            pass.error = "Please enter password"
        }else{
            firebaseLogin()
        }

     }

    private fun firebaseLogin() {
        progressBar.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressBar.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(requireContext(),"Loggin as $email",Toast.LENGTH_SHORT).show()
                //startActivity intent to profile

            }.addOnFailureListener {
                progressBar.dismiss()
                Toast.makeText(requireContext(),"Log in failed ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }

/*    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)


    }*/


}