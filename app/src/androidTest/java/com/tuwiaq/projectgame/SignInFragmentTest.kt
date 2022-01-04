package com.tuwiaq.projectgame

import android.text.TextUtils
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInFragmentTest{
    lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""


    @Before
    fun `setUp`(){
// mock vali //
        firebaseAuth = FirebaseAuth.getInstance()
        email = "aaa@gg.com"
        password = ""

    }

    @Test
    fun `login`(){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            password = "Please enter password"
        } else if (password.length < 6) {
            password = "Password must atleast 6 chracters long "
        }
    }
}