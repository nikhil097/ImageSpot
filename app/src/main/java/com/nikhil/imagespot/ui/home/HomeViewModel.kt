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

    var currentPage = 0
    var totalPages = -1
    var mIsPaginatedCall = false

    fun getPhotos(query: String, isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 0
            totalPages = -1
        }

        mIsPaginatedCall = !isRefresh

        if (!mIsPaginatedCall) _photosObserver.value = DataWrapper(isLoading = true)

        mCompositeDisposable.add(mImagesRepository.getPhotos(query, currentPage.plus(1).toString())
            .subscribe ({

                totalPages = it.pages
                currentPage = it.page

                _photosObserver.value = DataWrapper(response = it.photosList)
            }, {
                _photosObserver.value = DataWrapper(error = it)
            })
        )
    }

    fun hasMorePages(): Boolean = (currentPage != totalPages)
    fun isPaginatedCall(): Boolean = mIsPaginatedCall

    val photosObserver: LiveData<DataWrapper<List<Photo>>> = _photosObserver
}
