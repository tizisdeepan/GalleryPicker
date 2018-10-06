package com.picker.gallery.utils.scroll

/**
 * Created by deepan-5901 on 25/01/18.
 */
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.support.annotation.Keep
import android.text.TextUtils
import com.picker.gallery.utils.scroll.FastScrollRecyclerView
import com.picker.gallery.utils.scroll.FastScroller
import com.picker.gallery.utils.scroll.Utils

class FastScrollPopup(private val mRes: Resources, private val mRecyclerView: FastScrollRecyclerView) {

    private var mBackgroundSize: Int = 0
    private var mCornerRadius: Int = 0

    private val mBackgroundPath = Path()
    private val mBackgroundRect = RectF()
    private val mBackgroundPaint: Paint
    private var mBackgroundColor = -0x1000000

    private val mInvalidateRect = Rect()
    private val mTmpRect = Rect()

    private val mBgBounds = Rect()

    private var mSectionName: String? = null

    private val mTextPaint: Paint
    private val mTextBounds = Rect()

    var mPopupTextTypeface: Typeface? = null

    @get:Keep
    var alpha = 1f
        @Keep
        set(alpha) {
            field = alpha
            mRecyclerView.invalidate(mBgBounds)
        }

    private var mAlphaAnimator: ObjectAnimator? = null
    private var mVisible: Boolean = false

    var popupPosition: Int = 0

    val isVisible: Boolean
        get() = alpha > 0f && !TextUtils.isEmpty(mSectionName)

    init {

        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.alpha = 0

        if (mPopupTextTypeface != null) mTextPaint.typeface = mPopupTextTypeface

        setTextSize(Utils.toScreenPixels(mRes, 44f))
        setBackgroundSize(Utils.toPixels(mRes, 88f))
    }

    fun setBgColor(color: Int) {
        mBackgroundColor = color
        mBackgroundPaint.color = color
        mRecyclerView.invalidate(mBgBounds)
    }

    fun setTextColor(color: Int) {
        mTextPaint.color = color
        mRecyclerView.invalidate(mBgBounds)
    }

    fun setTextSize(size: Int) {
        mTextPaint.textSize = size.toFloat()
        mRecyclerView.invalidate(mBgBounds)
    }

    fun setBackgroundSize(size: Int) {
        mBackgroundSize = size
        mCornerRadius = mBackgroundSize / 2
        mRecyclerView.invalidate(mBgBounds)
    }

    fun setTypeface(typeface: Typeface) {
        mTextPaint.typeface = typeface
        mRecyclerView.invalidate(mBgBounds)
    }

    fun animateVisibility(visible: Boolean) {
        if (mVisible != visible) {
            mVisible = visible
            if (mAlphaAnimator != null) {
                mAlphaAnimator!!.cancel()
            }
            mAlphaAnimator = ObjectAnimator.ofFloat(this, "alpha", if (visible) 1f else 0f)
            mAlphaAnimator!!.duration = (if (visible) 200 else 150).toLong()
            mAlphaAnimator!!.start()
        }
    }

    private fun createRadii(): FloatArray {
        if (popupPosition == FastScroller.FastScrollerPopupPosition.CENTER) {
            return floatArrayOf(mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat())
        }

        return if (Utils.isRtl(mRes)) {
            floatArrayOf(mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), 0f, 0f)
        } else {
            floatArrayOf(mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), 0f, 0f, mCornerRadius.toFloat(), mCornerRadius.toFloat())
        }
    }

    @SuppressLint("WrongConstant")
    fun draw(canvas: Canvas) {
        if (isVisible) {
            // Draw the fast scroller popup
            val restoreCount = canvas.save()
            canvas.translate(mBgBounds.left.toFloat(), mBgBounds.top.toFloat())
            mTmpRect.set(mBgBounds)
            mTmpRect.offsetTo(0, 0)

            mBackgroundPath.reset()
            mBackgroundRect.set(mTmpRect)

            val radii = createRadii()

            mBackgroundPath.addRoundRect(mBackgroundRect, radii, Path.Direction.CW)

            mBackgroundPaint.alpha = (Color.alpha(mBackgroundColor) * alpha).toInt()
            mTextPaint.alpha = (alpha * 255).toInt()
            canvas.drawPath(mBackgroundPath, mBackgroundPaint)
            canvas.drawText(mSectionName!!, ((mBgBounds.width() - mTextBounds.width()) / 2).toFloat(),
                    (mBgBounds.height() - (mBgBounds.height() - mTextBounds.height()) / 2).toFloat(),
                    mTextPaint)
            canvas.restoreToCount(restoreCount)
        }
    }

    fun setSectionName(sectionName: String) {
        if (sectionName != mSectionName) {
            mSectionName = sectionName
            mTextPaint.getTextBounds(sectionName, 0, sectionName.length, mTextBounds)
            mTextBounds.right = (mTextBounds.left + mTextPaint.measureText(sectionName)).toInt()
        }
    }

    fun updateFastScrollerBounds(recyclerView: FastScrollRecyclerView, thumbOffsetY: Int): Rect {
        mInvalidateRect.set(mBgBounds)

        if (isVisible) {
            val edgePadding = recyclerView.scrollBarWidth
            val bgPadding = Math.round(((mBackgroundSize - mTextBounds.height()) / 10).toFloat()) * 5
            val bgHeight = mBackgroundSize
            val bgWidth = Math.max(mBackgroundSize, mTextBounds.width() + 2 * bgPadding)
            if (popupPosition == FastScroller.FastScrollerPopupPosition.CENTER) {
                mBgBounds.left = (recyclerView.width - bgWidth) / 2
                mBgBounds.right = mBgBounds.left + bgWidth
                mBgBounds.top = (recyclerView.height - bgHeight) / 2
            } else {
                if (Utils.isRtl(mRes)) {
                    mBgBounds.left = 2 * recyclerView.scrollBarWidth
                    mBgBounds.right = mBgBounds.left + bgWidth
                } else {
                    mBgBounds.right = recyclerView.width - 2 * recyclerView.scrollBarWidth
                    mBgBounds.left = mBgBounds.right - bgWidth
                }
                mBgBounds.top = thumbOffsetY - bgHeight + recyclerView.scrollBarThumbHeight / 2
                mBgBounds.top = Math.max(edgePadding, Math.min(mBgBounds.top, recyclerView.height - edgePadding - bgHeight))
            }
            mBgBounds.bottom = mBgBounds.top + bgHeight
        } else {
            mBgBounds.setEmpty()
        }
        mInvalidateRect.union(mBgBounds)
        return mInvalidateRect
    }
}