package com.nikhil.imagespot.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nikhil.imagespot.R
import com.nikhil.imagespot.inject.ViewModelFactory
import com.nikhil.imagespot.models.Photo
import com.nikhil.imagespot.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), ImagesListingAdapter.Callbacks {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: HomeViewModel
    var mPhotosList: MutableList<Photo> = arrayListOf()
    private lateinit var mPhotosAdapter: ImagesListingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setActivityTitle(getString(R.string.home))

        mPhotosAdapter = ImagesListingAdapter(mPhotosList)
        mPhotosAdapter.setCalbacks(this@HomeActivity)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(HomeViewModel::class.java)
        setupObservers()

        recyclerview_photos.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = mPhotosAdapter
        }

        mViewModel.getPhotos()
    }

    private fun setupObservers() {
        mViewModel.photosObserver.observe(this, Observer {data->
            data.run {
                showLoading(isLoading)
                response?.let {
                    if (it.isNotEmpty()) {
                        mPhotosAdapter.refreshData(it)
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