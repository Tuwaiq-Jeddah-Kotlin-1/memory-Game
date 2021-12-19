package com.tuwiaq.projectgame.model

import com.tuwiaq.projectgame.data.FlickerApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlickrBuilder {

    private const val BASE_URL ="https://api.flickr.com/services/rest/"

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) .build()


    val flickrApi: FlickerApi = retrofit().create(FlickerApi::class.java)
}