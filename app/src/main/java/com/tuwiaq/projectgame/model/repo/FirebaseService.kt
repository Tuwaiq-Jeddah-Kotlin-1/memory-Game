package com.tuwiaq.projectgame.model.repo


import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FirebaseUser
import com.tuwiaq.projectgame.data.FirebaseUser.Companion.toFirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object FirebaseService {
   // var loading:MutableLiveData<Boolean> = MutableLiveData()
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    private var customToken :String? = null
    private lateinit var application1:Application


    suspend fun getData(): FirebaseUser?{
        return try {
            auth.currentUser?.let {
                db.collection("uid")
                    .document(auth.currentUser!!.uid).get().await().toFirebaseUser()
            }
        }catch (e:Exception){
            null
        }
    }

    suspend fun forSignUp(email:String,password:String){
         try {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    val user = FirebaseUser(auth.currentUser!!.uid,email, password)
                    db.collection("uid").document(auth.currentUser?.uid!!).set(user)
                }else{
                    Log.i("else","Error to signUp",it.exception!!)
                }
            }
        }catch (e:Exception){
            null
        }
    }

    suspend fun forSignIn(email:String,password: String ,view: View):Boolean{

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                println("--------------------")
                println(email)
                if (it.isSuccessful){
                  //  Toast.makeText(application1,"welcome!",Toast.LENGTH_SHORT).show()


                    Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_mainFragment)
                  //  db.collection("uid").document(auth.currentUser!!.uid).get()
                   // val email = get_user!!.email
                }else{
                    Log.i("signIn",it.exception!!.message,it.exception!!)
                }
            }
            return true
        }catch (e:Exception){
            return false
        }
    }

    suspend fun forgetPassword(n_password: String,view: View){
        auth.sendPasswordResetEmail(n_password)
            ?.addOnCompleteListener {
               if (it.isSuccessful){
                   Navigation.findNavController(view).navigate(R.id.action_forgetPasswordFragment_to_mainFragment)

               }else{
                   Log.i("forget password failed",it.exception!!.message,it.exception!!)

               }
            }
    }

       fun signInState(){
           auth.currentUser != null
       }
    fun signOut(){
        auth.signOut()
    }






    fun f_app(application: Application){
        application1 = application
    }

}