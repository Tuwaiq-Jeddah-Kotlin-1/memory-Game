package com.tuwiaq.projectgame.data

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerApi {

    @GET("?method=flickr.interestingness.getList&api_key=2ef5e2f68997c0920c5a319503406019&format=json&nojsoncallback=1&extras=url_s")
    suspend fun fetchPhotos(@Query("per_page")i: Int):FlickerData
}