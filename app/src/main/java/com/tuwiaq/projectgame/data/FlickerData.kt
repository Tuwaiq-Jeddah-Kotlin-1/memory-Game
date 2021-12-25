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
    val title:String,
    val url_s:String,
)
data class Image2(
    var ul: String,

)
{
    companion object {
        fun DocumentSnapshot.toImage(): Image2? {
            try {
                val ul = getString("ul")!!

                return Image2(ul)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting toImage", e)
                return null
            }
        }

        private const val TAG = "Image1234"
    }
}
