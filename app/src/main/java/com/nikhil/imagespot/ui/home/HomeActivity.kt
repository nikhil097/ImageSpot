package com.nikhil.imagespot.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.fetchErrorMessage
import com.nikhil.imagespot.inject.ViewModelFactory
import com.nikhil.imagespot.models.Photo
import com.nikhil.imagespot.ui.base.BaseActivity
import com.nikhil.imagespot.ui.imagePreview.ImagePreviewActivity
import com.nikhil.imagespot.widgets.InfiniteRecyclerView
import com.nikhil.imagespot.widgets.LoadingFooterRVAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_thumbnail.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : BaseActivity(), PhotoListingAdapter.Callbacks {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: HomeViewModel
    var mPhotosList: MutableList<Any> = arrayListOf()
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

        val mLayoutManager = GridLayoutManager(this@HomeActivity, 3)
        mLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mPhotoAdapter.getItemViewType(position)) {
                    LoadingFooterRVAdapter.TYPE_ITEM -> 1
                    else -> 3
                }
            }
        }

        recyclerview_photos.apply {
            layoutManager = mLayoutManager
            adapter = mPhotoAdapter
            addOnScrollListener(object: InfiniteRecyclerView(mLayoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    mViewModel.getPhotos(edit_query.text.toString().trim())
                }
            })
        }
    }

    private fun setupObservers() {
        mViewModel.photosObserver.observe(this, Observer {data->
            data.run {
                if (!mViewModel.isPaginatedCall()) {
                    showLoading(isLoading)
                    response?.let {
                        if (it.isNotEmpty()) {
                            mPhotoAdapter.refreshData(it)
                            text_no_images.visibility = View.GONE
                            recyclerview_photos.visibility = View.VISIBLE
                            if (mViewModel.hasMorePages()) mPhotoAdapter.addLoadingFooter()
                        } else {
                            text_no_images.visibility = View.VISIBLE
                            recyclerview_photos.visibility = View.GONE
                        }
                    }
                    error?.let {
                        showError(it)
                    }
                } else {
                    response?.let {
                        addLoadingFooter(false)

                        if(it.isNotEmpty()) {
                            mPhotoAdapter.setData(it)
                            addLoadingFooter(mViewModel.hasMorePages())
                        }
                    }

                    error?.let {
                         showLoadingFooterError(it)
                    }
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
                mViewModel.getPhotos(it, isRefresh = true)
            }
    }

    private fun addLoadingFooter(active: Boolean) {
        if(active)
            mPhotoAdapter.addLoadingFooter()
        else
            mPhotoAdapter.removeLoadingFooter()
    }

    private fun showLoadingFooterError(throwable: Throwable) {
        mPhotoAdapter.showRetry(true, fetchErrorMessage(throwable))
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

    override fun onPhotoClick(photo: Photo, imageView: AppCompatImageView) {
        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@HomeActivity,imageView,photo.id);
        val intent = Intent(this@HomeActivity, ImagePreviewActivity::class.java).apply {
            putExtra(ImagePreviewActivity.EXTRA_PHOTO, photo)
        }
        startActivity(intent, activityOptionsCompat.toBundle())
    }

    override fun onRetryClick() {
        mViewModel.getPhotos(edit_query.text.toString().trim())
    }


}