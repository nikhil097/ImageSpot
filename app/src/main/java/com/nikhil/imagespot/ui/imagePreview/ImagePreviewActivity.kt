package com.nikhil.imagespot.ui.imagePreview

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
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
        isDisplayHomeAsUpEnabled = true

        mPhoto = intent.getParcelableExtra<Photo>(EXTRA_PHOTO)!!
        setActivityTitle(mPhoto.title)
        image.transitionName = mPhoto.id
        val photoUrlString = getString(R.string.image_url, mPhoto.farm, mPhoto.server, mPhoto.id, mPhoto.secret)

        Glide.with(this)
            .load(photoUrlString)
            .apply(
                RequestOptions.fitCenterTransform().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .priority(Priority.HIGH))
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    runOnUiThread {
                        image_progress.visibility = View.GONE
                        image.setImageBitmap((resource as BitmapDrawable).bitmap)
                    }
                    return true
                }

            }).submit()
    }

}