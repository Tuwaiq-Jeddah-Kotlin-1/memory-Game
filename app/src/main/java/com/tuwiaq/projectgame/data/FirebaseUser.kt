package com.tuwiaq.projectgame.data

import com.google.firebase.firestore.DocumentSnapshot


data class FirebaseUser(
    val uid:String,
    var email:String,
    var password:String
){
    companion object{
        fun DocumentSnapshot.toFirebaseUser():FirebaseUser?{
            return try {
                val uid = getString("uid")!!
                val email = getString("email")!!
                val password = getString("password")!!
                FirebaseUser(uid, email, password)
            }catch (e:Exception){
                null
            }
        }
    }
}