package com.picker.gallery.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class AnimationlessViewpager : ViewPager {

    var swipeLocked: Boolean = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(event: MotionEvent): Boolean = !swipeLocked && super.onTouchEvent(event)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean = !swipeLocked && super.onInterceptTouchEvent(event)

    override fun canScrollHorizontally(direction: Int): Boolean = !swipeLocked && super.canScrollHorizontally(direction)

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, false)
    }
}