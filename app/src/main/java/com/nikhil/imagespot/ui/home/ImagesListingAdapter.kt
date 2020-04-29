package com.nikhil.imagespot.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.loadImageUrl
import com.nikhil.imagespot.models.Photo
import kotlinx.android.synthetic.main.item_thumbnail.view.*

class ImagesListingAdapter(private var data: MutableList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callbacks {
        fun onPhotoClick(photo: Photo)
    }

    private var mCallbacks: Callbacks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_thumbnail, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindItem(holder as ImageViewHolder, data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun bindItem(holder: ImageViewHolder, photo: Photo, position: Int) {
        val photoUrlString = "http://farm${photo.farm}.static.flickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
        holder.imageThumbnail.loadImageUrl(imageUrl = photoUrlString, resourceId = R.color.colorGrey)
        holder.imageThumbnail.setOnClickListener {
            mCallbacks?.onPhotoClick(photo)
        }
    }

    fun setCalbacks(callbacks: Callbacks) {
        mCallbacks = callbacks;
    }

    fun refreshData(list: List<Photo>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageThumbnail = itemView.image_thumbnail
    }

}