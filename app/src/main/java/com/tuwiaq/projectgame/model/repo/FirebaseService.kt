package com.tuwiaq.projectgame.model.repo


import android.app.Application
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FirebaseUser
import com.tuwiaq.projectgame.data.FirebaseUser.Companion.toFirebaseUser
import com.tuwiaq.projectgame.ui.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import java.util.logging.SimpleFormatter

object FirebaseService {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebase: FirebaseAuth
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
            .addOnCompleteListener {
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
/*    fun score(score:String){
        val rootRef:FirebaseAuth = FirebaseAuth.getInstance()
        val ref = rootRef.currentUser?.uid
        val o = Firebase.storage.reference.child("score $ref")
        val map: MutableMap<String, Any> = HashMap()
        map["score"] = 0


    }*/

    suspend fun deleteImage(context:Context){
        try {

            (auth.currentUser?.uid?.let { it1 ->
                db.collection("ImageUrl").document(
                    it1
                ).delete()
            }?.addOnSuccessListener {
               Toast.makeText(context, "Successfully deleted image", Toast.LENGTH_SHORT).show()

            }?.addOnFailureListener {
                Toast.makeText(context, "failed to delete the image", Toast.LENGTH_SHORT).show()

            })
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
               Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    suspend fun addImage(imageUri: Uri){
        val formatter = SimpleDateFormat("yyy-MM-dd-HH-mm-ss")
        val now = Date()
        val fileName = formatter.format(now)
        val storageRef = Firebase.storage.reference.child("images/$fileName.jpg")

        try {
            storageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        //Add event to FireStore
                        val docData = hashMapOf(
                            "ul" to it.toString()
                        )
                        println("-----------------------------------")
                        println(auth.currentUser?.uid)
                        auth.currentUser?.uid?.let { it1 ->
                            db.collection("ImageUrl").document(
                                it1

                            ).set(docData)
                        }

                    }.addOnFailureListener {
                        Log.e(MainFragment.Tag, "Error upload the image ", it)

                    }
                }


        } catch (e: Exception) {
            Log.e(MainFragment.Tag, "Error add the event ", e)

        }
    }


}