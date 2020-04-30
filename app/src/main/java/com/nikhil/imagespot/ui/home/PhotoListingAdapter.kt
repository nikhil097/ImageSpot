package com.nikhil.imagespot.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.inflateView
import com.nikhil.imagespot.extensions.loadImageUrl
import com.nikhil.imagespot.models.Photo
import com.nikhil.imagespot.widgets.LoadingFooterRVAdapter
import kotlinx.android.synthetic.main.item_thumbnail.view.*

class PhotoListingAdapter(private var data: MutableList<Any>) : LoadingFooterRVAdapter(data) {

    interface Callbacks: LoadingFooterRVAdapter.Callbacks {
        fun onPhotoClick(photo: Photo, imageView: AppCompatImageView)
    }

    private var mCallbacks: Callbacks? = null

    override fun createItemView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> ImageViewHolder(inflateView(R.layout.item_thumbnail, parent, false))
            else -> throw IllegalArgumentException("Unknown view type.")
        }
    }

    override fun bindItem(holder: RecyclerView.ViewHolder, any: Any, position: Int) {
        when(getItemViewType(position)){
            TYPE_ITEM -> bindItem(holder as ImageViewHolder, data[position] as Photo, position)
            else -> throw IllegalArgumentException("Unknown view type.")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onRetryClick() {
        mCallbacks?.onRetryClick()
    }

    private fun bindItem(holder: ImageViewHolder, photo: Photo, position: Int) {
        val photoUrlString = "http://farm${photo.farm}.static.flickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
        holder.imageThumbnail.loadImageUrl(imageUrl = photoUrlString, resourceId = R.color.colorGrey)
        holder.imageThumbnail.transitionName = photo.id
        holder.imageThumbnail.setOnClickListener {
            mCallbacks?.onPhotoClick(photo, holder.imageThumbnail)
        }
    }

    fun setCalbacks(callbacks: Callbacks) {
        mCallbacks = callbacks;
    }

    fun setData(list: List<Any>) {
        val oldSize = data.size
        data.addAll(list)
        notifyItemRangeInserted(oldSize, data.size)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageThumbnail = itemView.image_thumbnail
    }

}