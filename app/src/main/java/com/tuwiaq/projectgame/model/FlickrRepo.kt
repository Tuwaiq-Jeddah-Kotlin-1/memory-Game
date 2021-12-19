package com.tuwiaq.projectgame.model

import com.tuwiaq.projectgame.data.FlickerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlickrRepo {

    private val api = FlickrBuilder.flickrApi

    suspend fun fetchIntrestingList(i: Int): FlickerData = withContext(Dispatchers.IO){
        api.fetchPhotos(i)

    }

}