package com.nikhil.imagespot.widgets

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.inflateView
import kotlinx.android.synthetic.main.item_loading.view.*

abstract class LoadingFooterRVAdapter(private var data: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class Loader {
        // dummy class object to identify it as loading element
    }

    interface Callbacks {
        fun onRetryClick()
    }

   companion object {
       val TYPE_ITEM = 1
       private const val TYPE_LOADING = 2

   }

    private var isLoadingAdded = false
    private var retryPageLoad = false
    private var errorMsg: String? = null
    var context: Context? = null

    protected abstract fun createItemView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun bindItem(holder: RecyclerView.ViewHolder, any: Any, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == TYPE_LOADING) {
            return LoadingViewHolder(inflateView(R.layout.item_loading, parent, false))
        } else {
            return createItemView(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_LOADING -> bindLoading(holder as LoadingViewHolder, data[position])
            else -> bindItem(holder, data[position], position)
        }
    }

    private fun bindLoading(holder: LoadingViewHolder, any: Any) {
        if (retryPageLoad) {
            holder.errorLayout.visibility = View.VISIBLE
            holder.progressBar.visibility = View.GONE

            holder.errorText.text = if (errorMsg != null)
                errorMsg
            else
                context!!.getString(R.string.unexpected_error)

        } else {
            holder.errorLayout.visibility = View.GONE
            holder.progressBar.visibility = View.VISIBLE
        }

        holder.errorLayout.setOnClickListener{
            showRetry(false, null)
            onRetryClick()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size - 1 && isLoadingAdded && getItem(position) is Loader) TYPE_LOADING else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData(list: List<Any>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
        isLoadingAdded = false
    }

    open fun addLoadingFooter() {
        if(!isLoadingAdded) {
            data.add(Loader())
        }
        isLoadingAdded = true
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false

            val position = data.size - 1
            val result = getItem(position)

            if (result != null && result is Loader) {
                data.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int): Any? {
        return data[position]
    }

    fun showRetry(show: Boolean, errorMsg: String?) {
        Log.d("showRetry", "->"+errorMsg)
        retryPageLoad = show
        notifyItemChanged(data.size - 1)

        if (errorMsg != null) this.errorMsg = errorMsg
    }

    abstract fun onRetryClick()

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val errorLayout = itemView.errorLayout_loadmore
        val errorText = itemView.text_loadmore_error
        val progressBar = itemView.progress_loadmore
    }

}