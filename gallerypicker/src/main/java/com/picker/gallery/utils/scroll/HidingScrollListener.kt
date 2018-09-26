package com.picker.gallery.utils.scroll

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class HidingScrollListener : RecyclerView.OnScrollListener {

    var mControlsVisible = true
    private var HIDE_THRESHOLD = 50
    private var mScrolledDistance = 0

    constructor(threshold: Int) {
        HIDE_THRESHOLD = threshold
    }

    private constructor() {}

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (firstVisibleItem == 0) {
            if (!mControlsVisible) {
                onShow()
                mControlsVisible = true
            }
        } else {
            if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                onHide()
                mControlsVisible = false
                mScrolledDistance = 0
            } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                onShow()
                mControlsVisible = true
                mScrolledDistance = 0
            }
        }
        if (mControlsVisible && dy > 0 || !mControlsVisible && dy < 0) {
            mScrolledDistance += dy
        }
    }

    fun resetScrollDistance() {
        mControlsVisible = true
        mScrolledDistance = 0
    }

    abstract fun onHide()

    abstract fun onShow()
}