package com.tuwiaq.projectgame.data

import com.google.gson.annotations.SerializedName

data class FlickerData (//root with data type
    val photos:FlickrListPhotos
)

data class FlickrListPhotos( //list of photo
    val photo : List<FlickerPhoto>
)
data class FlickerPhoto (
    val title:String,
    @SerializedName("url_s")
    val url:String,
)