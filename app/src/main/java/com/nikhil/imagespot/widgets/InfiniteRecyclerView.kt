package com.nikhil.imagespot.widgets

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteRecyclerView: RecyclerView.OnScrollListener {

    var TAG: String = InfiniteRecyclerView::class.java.getSimpleName()

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.

    private var visibleThreshold = 2 // The minimum amount of items to have below your current scroll position before loading more.

    private var firstVisibleItem: Int = 0
    private  var visibleItemCount:Int = 0
    private  var totalItemCount:Int = 0

    private var currentPage = 1

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    constructor(layoutManager: LinearLayoutManager?) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        // Increase visible threshold based on number of columns
        visibleThreshold *= layoutManager.spanCount
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = mLayoutManager!!.itemCount

        // Check the layout manager type in order to determine the last visible position
        if (mLayoutManager is LinearLayoutManager) {
            firstVisibleItem = (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else if (mLayoutManager is GridLayoutManager) {
            firstVisibleItem = (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold
        ) {
            // End has been reached
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    fun setVisibleThreshold(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }

    fun setCurrentPage(current_page: Int) {
        currentPage = current_page
        previousTotal = 0
    }

    abstract fun onLoadMore(currentPage: Int)

}