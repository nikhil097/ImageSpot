package com.nikhil.imagespot.ui.imagePreview

import android.os.Bundle
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.loadImageUrl
import com.nikhil.imagespot.models.Photo
import com.nikhil.imagespot.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : BaseActivity() {

    companion object {
        const val EXTRA_PHOTO = "EXTRA_PHOTO"
    }

    private lateinit var mPhoto: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        setActivityTitle(getString(R.string.home))

        mPhoto = intent.getParcelableExtra<Photo>(EXTRA_PHOTO)!!
        image.transitionName = mPhoto.id
        val photoUrlString = "http://farm${mPhoto.farm}.static.flickr.com/${mPhoto.server}/${mPhoto.id}_${mPhoto.secret}.jpg"
        image.loadImageUrl(photoUrlString)

    }

}