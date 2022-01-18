package com.tuwiaq.projectgame.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot



data class FlickerData (//root with data type
    val photos:FlickrListPhotos
)

data class FlickrListPhotos( //list of photo
    val photo : List<FlickerPhoto>
)
data class FlickerPhoto (
    val id:String,
    val title:String,
    val url_s:String,
)
