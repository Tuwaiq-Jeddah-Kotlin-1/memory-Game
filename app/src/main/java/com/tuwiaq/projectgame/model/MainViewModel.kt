package com.tuwiaq.projectgame.model

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FirebaseUser
import com.tuwiaq.projectgame.data.FlickerData
import com.tuwiaq.projectgame.model.repo.FirebaseService
import com.tuwiaq.projectgame.ui.GameAdapter
import kotlinx.coroutines.launch

class MainViewModel(context: Application) : AndroidViewModel(context) {
    private val firebaseAccunt = MutableLiveData<FirebaseUser>()
    val acc :LiveData<FirebaseUser> = firebaseAccunt

    init {
        viewModelScope.launch {
            firebaseAccunt.value = FirebaseService.getData()
        }
    }

    fun signUp(email:String,password:String) = viewModelScope.launch {
        FirebaseService.forSignUp(email, password)
    }

    fun  signIn(email: String,password: String,view: View) = viewModelScope.launch {
        FirebaseService.forSignIn(email, password,view)
    }

    fun signOutFromAcc() = viewModelScope.launch {
        FirebaseService.signOut()
    }

    fun resetUserPassword(n_password:String,view: View) = viewModelScope.launch {
        FirebaseService.forgetPassword(n_password,view)
    }

    fun deleteImags(context: Context) = viewModelScope.launch {
        FirebaseService.deleteImage(context)
    }

    fun addImage(image:Uri) = viewModelScope.launch {
        FirebaseService.addImage(image)
    }


    val sharedPreference = context.getSharedPreferences(
        GameAdapter.STOCK_SHARED_KEY,
        Context.MODE_PRIVATE)
    var editor = sharedPreference.edit()

    fun saveScore(score1:Int){
        editor.putInt("username", getSaveSco().toInt() + score1)
        editor.apply()
    }
    fun getSaveSco() = sharedPreference.getInt("username", 0).toString()
    var gameScore = 0



    val repo = FlickrRepo()

    fun fetchIntrestingList(i: Int): LiveData<FlickerData> {
        var photos = MutableLiveData<FlickerData>()
        viewModelScope.launch {
            try {
                photos.postValue(repo.fetchIntrestingList(i))

            } catch (e: Throwable) {

                Log.e("Flickr Images", " problem: ${e.localizedMessage}")
            }
        }
        return photos
    }






}
