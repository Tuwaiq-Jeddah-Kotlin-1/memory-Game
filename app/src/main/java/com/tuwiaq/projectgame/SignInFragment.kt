package com.tuwiaq.projectgame

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class signInFragment : Fragment() {

    private lateinit var progressBar: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
private lateinit var  signInBtn:Button
private lateinit var emailET :EditText
private lateinit var passwordEt :EditText
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInBtn = view.findViewById(R.id.signInBtn)
        passwordEt = view.findViewById(R.id.passwordEt)
        emailET = view.findViewById(R.id.emailET)


        progressBar = ProgressDialog(context)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Creating Account In...")
        progressBar.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            validateData()

            findNavController().navigate(R.id.mainFragment)

            refreshCurrentFragment()
        }
        return view
    }

    private fun validateData() {
        email = emailET.text.toString().trim()
        password = passwordEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.error = "Invalid email format"
        }else if (TextUtils.isEmpty(password)){
            passwordEt.error = "Please enter password"
        }else if(password.length < 6){
            passwordEt.error = "Password must atleast 6 chracters long "
        }else{
            firebaseSignUp()
        }
    }

    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }

    private fun firebaseSignUp() {
        progressBar.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressBar.dismiss()
                val firebase = firebaseAuth.currentUser
                val email = firebase!!.email
                Toast.makeText(requireContext(),"Account Created with $email", Toast.LENGTH_SHORT).show()
                // activity intent profile


            }.addOnFailureListener {
                progressBar.dismiss()
                Toast.makeText(requireContext(),"Sign Up failed ", Toast.LENGTH_SHORT).show()
            }
    }

}