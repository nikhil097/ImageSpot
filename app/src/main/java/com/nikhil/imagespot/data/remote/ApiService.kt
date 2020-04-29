package com.nikhil.imagespot.data.remote

import com.nikhil.imagespot.models.PhotoResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("services/rest")
    fun getPhotos(@QueryMap queryMap: Map<String, String>): Observable<PhotoResponse>

}