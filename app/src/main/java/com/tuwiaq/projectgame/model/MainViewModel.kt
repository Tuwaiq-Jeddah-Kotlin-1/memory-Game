package com.tuwiaq.projectgame.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.tuwiaq.projectgame.data.FlickerData
import com.tuwiaq.projectgame.ui.GameAdapter
import kotlinx.coroutines.launch

class MainViewModel(context: Application) : AndroidViewModel(context) {
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
  /*  fun getImageById(ImageId: String) = viewModelScope.launch {
        _image.value = GameAdapter.FirebaseEventService.getImageData(ImageId)
    }*/
}
