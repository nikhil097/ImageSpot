package com.nikhil.imagespot.data

import androidx.collection.arrayMapOf
import com.nikhil.imagespot.data.remote.ApiService
import com.nikhil.imagespot.models.PhotoResponse
import com.nikhil.imagespot.models.PhotosData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImagesRepository@Inject constructor(private val mApiService: ApiService) {

    fun getPhotos(query: String): Observable<PhotosData> {

        val map = arrayMapOf<String, String>()
        map["method"] = "flickr.photos.search"
        map["text"] = query
        return mApiService.getPhotos(map)
            .map { it.photosData }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}