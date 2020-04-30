package com.nikhil.imagespot.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.nikhil.imagespot.ImageSpotApplication
import com.nikhil.imagespot.R
import com.nikhil.imagespot.data.remote.ErrorResponse
import com.nikhil.imagespot.data.remote.ErrorUtils
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeoutException


fun inflateView(@LayoutRes layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean): View {
    return LayoutInflater.from(parent.context).inflate(layoutResId, parent, attachToRoot)
}

fun fetchErrorMessage(throwable: Throwable): String {
    return when (throwable) {
        is IOException -> getString(ImageSpotApplication.instance, R.string.error_msg_no_internet)
        is TimeoutException -> getString(ImageSpotApplication.instance, R.string.error_msg_timeout)
        else -> getErrorMessage(throwable) ?: getString(ImageSpotApplication.instance, R.string.unexpected_error)
    }
}

fun getError(throwable: Throwable): ErrorResponse? {
    var response: ErrorResponse? = null
    if(throwable is HttpException) {
        try {
            response = ErrorUtils.parseError(throwable.response().errorBody()!!.string(), ErrorResponse::class.java)
        } catch (exception: Exception) {
            // do nothing
        }
    }
    return response
}

fun getErrorMessage(throwable: Throwable): String? {
    var message: String? = null
    whenNotNull(getError(throwable)) {
        message = it.message
    }
    return message
}


fun getSnackBar(view: View, value: String): Snackbar {
    return getSnackBar(view, value, Snackbar.LENGTH_LONG)
}

fun getSnackBar(view: View, value: String, length: Int): Snackbar {
    return Snackbar.make(view, value, length)
}

fun showSnackBar(view: View, value: String) {
    getSnackBar(view, value).show()
}

fun getString(context: Context?, stringRes: Int) : String {
    return context!!.resources.getString(stringRes)
}

inline fun <T:Any, R> whenNotNull(input: T?, callback: (T)->R): R? {
    return input?.let(callback)
}

fun ImageView.loadImageUrl(imageUrl: String, requestOptions: RequestOptions = RequestOptions.centerCropTransform(), roundedCorners: Boolean = false, resourceId: Int? = null) {
    val options = if(roundedCorners) {
        RequestOptions().transform(CenterCrop(), RoundedCorners(8))
    } else requestOptions

    if (resourceId != null) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(resourceId)
            .apply(options.priority(Priority.HIGH))
            .into(this)
    } else {
        Glide.with(this)
            .load(imageUrl)
            .apply(options.priority(Priority.HIGH))
            .into(this)
    }
}