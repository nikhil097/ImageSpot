package com.nikhil.imagespot.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nikhil.imagespot.R
import com.nikhil.imagespot.inject.ViewModelFactory
import com.nikhil.imagespot.models.Photo
import com.nikhil.imagespot.ui.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : BaseActivity(), PhotoListingAdapter.Callbacks {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: HomeViewModel
    var mPhotosList: MutableList<Photo> = arrayListOf()
    private lateinit var mPhotoAdapter: PhotoListingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setActivityTitle(getString(R.string.home))

        mPhotoAdapter = PhotoListingAdapter(mPhotosList)
        mPhotoAdapter.setCalbacks(this@HomeActivity)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(HomeViewModel::class.java)
        setupObservers()
        addOnTextChangeObserver(edit_query)

        recyclerview_photos.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = mPhotoAdapter
        }

        mViewModel.getPhotos("apple")
    }

    private fun setupObservers() {
        mViewModel.photosObserver.observe(this, Observer {data->
            data.run {
                showLoading(isLoading)
                response?.let {
                    if (it.isNotEmpty()) {
                        mPhotoAdapter.refreshData(it)
                        text_no_images.visibility = View.GONE
                        recyclerview_photos.visibility = View.VISIBLE
                    } else {
                        text_no_images.visibility = View.VISIBLE
                        recyclerview_photos.visibility = View.GONE
                    }
                }
                error?.let {
                    showError(it)
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun addOnTextChangeObserver(editText: EditText) {
        RxTextView.textChangeEvents(editText)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it.text().length > 1 }
            .map<String> { textViewTextChangeEvent -> textViewTextChangeEvent.text().toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mViewModel.getPhotos(it)
            }
    }

    override fun showLoading(active: Boolean) {
        if (active) {
            layout_shimmer.visibility = View.VISIBLE
            layout_shimmer.startShimmer()
        } else {
            layout_shimmer.visibility = View.GONE
            layout_shimmer.stopShimmer()
        }
    }

    override fun onPhotoClick(photo: Photo) {

    }


}