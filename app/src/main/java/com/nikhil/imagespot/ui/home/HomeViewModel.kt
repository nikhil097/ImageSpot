package com.nikhil.imagespot.ui.home

import androidx.lifecycle.ViewModel
import com.nikhil.imagespot.data.ImagesRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel@Inject constructor(private val mImagesRepository: ImagesRepository): ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()


}
