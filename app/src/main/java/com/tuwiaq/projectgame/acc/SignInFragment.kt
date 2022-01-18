package com.tuwiaq.projectgame.acc

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.model.MainViewModel

class SignInFragment : Fragment() {

    private lateinit var progressBar: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signInBtn: Button
    private lateinit var emailET: EditText
    private lateinit var passwordEt: EditText
    private var email = ""
    private var password = ""
    private val acc_vm: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInBtn = view.findViewById(R.id.signInBtn)
        passwordEt = view.findViewById(R.id.passwordEt)
        emailET = view.findViewById(R.id.emailET)


        progressBar = ProgressDialog(context)
        progressBar.setTitle(getString(R.string.Please_wait))
        progressBar.setMessage(getString(R.string.Logging_in))
        progressBar.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            validateData()

            findNavController().navigate(R.id.mainFragment)

            refreshCurrentFragment()
        }
        return view
    }

    fun validateData() {

        email = emailET.text.toString().trim()
        password = passwordEt.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(
                requireContext(),
                "Password must be atleast 6 chatcters long",
                Toast.LENGTH_LONG
            ).show()
        } else {

            acc_vm.signUp(email, password)
        }

    }


    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.+)(\\.)(.{1,})"
   // val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

    fun boo(email: String, password: String): Boolean {

        return EMAIL_REGEX.toRegex().matches(email) && (password.contains(Regex("[A-Z]"))&& password.contains(Regex("[0-9]")) )
    }


    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }


}