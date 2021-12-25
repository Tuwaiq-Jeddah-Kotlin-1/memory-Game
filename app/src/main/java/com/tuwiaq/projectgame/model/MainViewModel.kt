package com.tuwiaq.projectgame.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwiaq.projectgame.data.FlickerData
import com.tuwiaq.projectgame.ui.GameAdapter
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // val vmm : LiveData<FlickerData>
  //  private val _image = MutableLiveData<ImageUp>()
 //   val ImageT: LiveData<ImageUp> = _image
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
