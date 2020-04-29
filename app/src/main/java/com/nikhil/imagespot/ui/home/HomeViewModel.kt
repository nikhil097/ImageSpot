package com.nikhil.imagespot.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikhil.imagespot.data.ImagesRepository
import com.nikhil.imagespot.data.remote.DataWrapper
import com.nikhil.imagespot.models.Photo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel@Inject constructor(private val mImagesRepository: ImagesRepository): ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()
    private val _photosObserver = MutableLiveData<DataWrapper<List<Photo>>>()

    fun getPhotos() {
        _photosObserver.value = DataWrapper(isLoading = true)
        mCompositeDisposable.add(mImagesRepository.getPhotos("text")
            .subscribe ({
                _photosObserver.value = DataWrapper(response = it.photosList)
            }, {
                _photosObserver.value = DataWrapper(error = it)
            })
        )
    }

    val photosObserver: LiveData<DataWrapper<List<Photo>>> = _photosObserver
}
