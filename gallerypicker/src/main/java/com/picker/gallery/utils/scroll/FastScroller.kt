package com.picker.gallery.utils.scroll

/**
 * Created by deepan-5901 on 25/01/18.
 */
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.annotation.Keep
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.picker.gallery.R
import com.zoho.zohosocial.utils.scroll.FastScrollPopup

class FastScroller(context: Context, private val mRecyclerView: FastScrollRecyclerView?, attrs: AttributeSet) {
    private val mHideRunnable: Runnable
    private val mThumbInactiveColor = 0x79000000
    var mThumbPosition = Point(-1, -1)
    var mOffset = Point(0, 0)
    internal var mAnimatingShow: Boolean = false
    private val mPopup: FastScrollPopup
    val thumbHeight: Int
    val width: Int
    private val mThumb: Paint
    private val mTrack: Paint
    private val mTmpRect = Rect()
    private val mInvalidateRect = Rect()
    private val mInvalidateTmpRect = Rect()
    private val mTouchInset: Int
    private var mTouchOffset: Int = 0
    var isDragging: Boolean = false
        private set
    private var mAutoHideAnimator: Animator? = null
    private var mAutoHideDelay = DEFAULT_AUTO_HIDE_DELAY
    private var mAutoHideEnabled = true
    private var mThumbActiveColor: Int = 0
    private var mThumbInactiveState: Boolean = false

    var offsetX: Int
        @Keep
        get() = mOffset.x
        @Keep
        set(x) = setOffset(x, mOffset.y)

    init {

        val resources = context.resources
        mPopup = FastScrollPopup(resources, mRecyclerView!!)

        thumbHeight = Utils.toPixels(resources, 40f)
        width = Utils.toPixels(resources, 5f)

        mTouchInset = Utils.toPixels(resources, -24f)

        mThumb = Paint(Paint.ANTI_ALIAS_FLAG)
        mTrack = Paint(Paint.ANTI_ALIAS_FLAG)

        val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.FastScrollRecyclerView, 0, 0)
        try {
            mAutoHideEnabled = typedArray.getBoolean(R.styleable.FastScrollRecyclerView_fastScrollAutoHide, true)
            mAutoHideDelay = typedArray.getInteger(R.styleable.FastScrollRecyclerView_fastScrollAutoHideDelay, DEFAULT_AUTO_HIDE_DELAY)
            mThumbInactiveState = typedArray.getBoolean(R.styleable.FastScrollRecyclerView_fastScrollThumbInactiveColor, false)
            mThumbActiveColor = typedArray.getColor(R.styleable.FastScrollRecyclerView_fastScrollThumbColor, 0x79000000)

            val trackColor = typedArray.getColor(R.styleable.FastScrollRecyclerView_fastScrollTrackColor, 0x28000000)
            val popupBgColor = typedArray.getColor(R.styleable.FastScrollRecyclerView_fastScrollPopupBgColor, -0x1000000)
            val popupTextColor = typedArray.getColor(R.styleable.FastScrollRecyclerView_fastScrollPopupTextColor, -0x1)
            val popupTextSize = typedArray.getDimensionPixelSize(R.styleable.FastScrollRecyclerView_fastScrollPopupTextSize, Utils.toScreenPixels(resources, 44f))
            val popupBackgroundSize = typedArray.getDimensionPixelSize(R.styleable.FastScrollRecyclerView_fastScrollPopupBackgroundSize, Utils.toPixels(resources, 88f))
            val popupPosition = typedArray.getInteger(R.styleable.FastScrollRecyclerView_fastScrollPopupPosition, FastScrollerPopupPosition.ADJACENT)

            mTrack.color = trackColor
            mThumb.color = if (mThumbInactiveState) mThumbInactiveColor else mThumbActiveColor
            mPopup.setBgColor(popupBgColor)
            mPopup.setTextColor(popupTextColor)
            mPopup.setTextSize(popupTextSize)
            mPopup.setBackgroundSize(popupBackgroundSize)
            mPopup.popupPosition = popupPosition
        } finally {
            typedArray.recycle()
        }

        mHideRunnable = Runnable {
            if (!isDragging) {
                if (mAutoHideAnimator != null) {
                    mAutoHideAnimator!!.cancel()
                }
                mAutoHideAnimator = ObjectAnimator.ofInt(this@FastScroller, "offsetX", (if (Utils.isRtl(mRecyclerView.resources)) -1 else 1) * width)
                mAutoHideAnimator!!.interpolator = FastOutLinearInInterpolator()
                mAutoHideAnimator!!.duration = 200
                mAutoHideAnimator!!.start()
            }
        }

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!mRecyclerView.isInEditMode) {
                    show()
                }
            }
        })

        if (mAutoHideEnabled) {
            postAutoHideDelayed()
        }
    }

    fun handleTouchEvent(ev: MotionEvent, downX: Int, downY: Int, lastY: Int,
                         stateChangeListener: OnFastScrollStateChangeListener?) {
        val config = ViewConfiguration.get(mRecyclerView!!.context)

        val action = ev.action
        val y = ev.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> if (isNearPoint(downX, downY)) {
                mTouchOffset = downY - mThumbPosition.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragging && isNearPoint(downX, downY) &&
                        Math.abs(y - downY) > config.scaledTouchSlop) {
                    mRecyclerView.parent.requestDisallowInterceptTouchEvent(true)
                    isDragging = true
                    mTouchOffset += lastY - downY
                    mPopup.animateVisibility(true)
                    stateChangeListener?.onFastScrollStart()
                    if (mThumbInactiveState) {
                        mThumb.color = mThumbActiveColor
                    }
                }
                if (isDragging) {
                    val top = 0
                    val bottom = mRecyclerView.height - thumbHeight
                    val boundedY = Math.max(top, Math.min(bottom, y - mTouchOffset)).toFloat()
                    val sectionName = mRecyclerView.scrollToPositionAtProgress((boundedY - top) / (bottom - top))
                    mPopup.setSectionName(sectionName)
                    mPopup.animateVisibility(!sectionName.isEmpty())
                    mRecyclerView.invalidate(mPopup.updateFastScrollerBounds(mRecyclerView, mThumbPosition.y))
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mTouchOffset = 0
                if (isDragging) {
                    isDragging = false
                    mPopup.animateVisibility(false)
                    stateChangeListener?.onFastScrollStop()
                }
                if (mThumbInactiveState) {
                    mThumb.color = mThumbInactiveColor
                }
            }
        }
    }

    fun draw(canvas: Canvas) {

        if (mThumbPosition.x < 0 || mThumbPosition.y < 0) {
            return
        }

        //Background of scroll indicator
        canvas.drawRect((mThumbPosition.x + mOffset.x).toFloat(), mOffset.y.toFloat(), (mThumbPosition.x + mOffset.x + width).toFloat(), (mRecyclerView!!.height + mOffset.y).toFloat(), mTrack)

        //Handle of scroll indicator
        canvas.drawRect((mThumbPosition.x + mOffset.x).toFloat(), (mThumbPosition.y + mOffset.y).toFloat(), (mThumbPosition.x + mOffset.x + width).toFloat(), (mThumbPosition.y + mOffset.y + thumbHeight).toFloat(), mThumb)

        //Popup of scroll indicator
        mPopup.draw(canvas)
    }

    private fun isNearPoint(x: Int, y: Int): Boolean {
        mTmpRect.set(mThumbPosition.x, mThumbPosition.y, mThumbPosition.x + width, mThumbPosition.y + thumbHeight)
        mTmpRect.inset(mTouchInset, mTouchInset)
        return mTmpRect.contains(x, y)
    }

    fun setThumbPosition(x: Int, y: Int) {
        if (mThumbPosition.x == x && mThumbPosition.y == y) {
            return
        }
        mInvalidateRect.set(mThumbPosition.x + mOffset.x, mOffset.y, mThumbPosition.x + mOffset.x + width, mRecyclerView!!.height + mOffset.y)
        mThumbPosition.set(x, y)
        mInvalidateTmpRect.set(mThumbPosition.x + mOffset.x, mOffset.y, mThumbPosition.x + mOffset.x + width, mRecyclerView.height + mOffset.y)
        mInvalidateRect.union(mInvalidateTmpRect)
        mRecyclerView.invalidate(mInvalidateRect)
    }

    fun setOffset(x: Int, y: Int) {
        if (mOffset.x == x && mOffset.y == y) {
            return
        }
        mInvalidateRect.set(mThumbPosition.x + mOffset.x, mOffset.y, mThumbPosition.x + mOffset.x + width, mRecyclerView!!.height + mOffset.y)
        mOffset.set(x, y)
        mInvalidateTmpRect.set(mThumbPosition.x + mOffset.x, mOffset.y, mThumbPosition.x + mOffset.x + width, mRecyclerView.height + mOffset.y)
        mInvalidateRect.union(mInvalidateTmpRect)
        mRecyclerView.invalidate(mInvalidateRect)
    }

    fun show() {
        if (!mAnimatingShow) {
            if (mAutoHideAnimator != null) {
                mAutoHideAnimator!!.cancel()
            }
            mAutoHideAnimator = ObjectAnimator.ofInt(this, "offsetX", 0)
            mAutoHideAnimator!!.interpolator = LinearOutSlowInInterpolator()
            mAutoHideAnimator!!.duration = 150
            mAutoHideAnimator!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    mAnimatingShow = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mAnimatingShow = false
                }
            })
            mAnimatingShow = true
            mAutoHideAnimator!!.start()
        }
        if (mAutoHideEnabled) {
            postAutoHideDelayed()
        } else {
            cancelAutoHide()
        }
    }

    private fun postAutoHideDelayed() {
        if (mRecyclerView != null) {
            cancelAutoHide()
            mRecyclerView.postDelayed(mHideRunnable, mAutoHideDelay.toLong())
        }
    }

    private fun cancelAutoHide() {
        mRecyclerView?.removeCallbacks(mHideRunnable)
    }

    fun setThumbColor(@ColorInt color: Int) {
        mThumb.color = color
        mRecyclerView!!.invalidate(mInvalidateRect)
    }

    fun setTrackColor(@ColorInt color: Int) {
        mTrack.color = color
        mRecyclerView!!.invalidate(mInvalidateRect)
    }

    fun setPopupBgColor(@ColorInt color: Int) {
        mPopup.setBgColor(color)
    }

    fun setPopupTextColor(@ColorInt color: Int) {
        mPopup.setTextColor(color)
    }

    fun setPopupTypeface(typeface: Typeface) {
        mPopup.setTypeface(typeface)
    }

    fun setPopupTextSize(size: Int) {
        mPopup.setTextSize(size)
    }

    fun setAutoHideDelay(hideDelay: Int) {
        mAutoHideDelay = hideDelay
        if (mAutoHideEnabled) {
            postAutoHideDelayed()
        }
    }

    fun setAutoHideEnabled(autoHideEnabled: Boolean) {
        mAutoHideEnabled = autoHideEnabled
        if (autoHideEnabled) {
            postAutoHideDelayed()
        } else {
            cancelAutoHide()
        }
    }

    fun setPopupPosition(popupPosition: Int) {
        mPopup.popupPosition = popupPosition
    }

    fun setThumbInactiveColor(thumbInactiveColor: Boolean) {
        mThumbInactiveState = thumbInactiveColor
        mThumb.color = if (mThumbInactiveState) mThumbInactiveColor else mThumbActiveColor
    }

    fun setPopupTextTypeface(typeface: Typeface){
        mPopup.mPopupTextTypeface = typeface
    }

    class FastScrollerPopupPosition {
        companion object {
            val ADJACENT = 0
            val CENTER = 1
        }
    }

    companion object {
        private val DEFAULT_AUTO_HIDE_DELAY = 1500
    }
}
