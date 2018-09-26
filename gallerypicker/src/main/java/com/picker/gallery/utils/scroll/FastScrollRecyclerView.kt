package com.picker.gallery.utils.scroll

/**
 * Created by deepan-5901 on 25/01/18.
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent

class FastScrollRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr), RecyclerView.OnItemTouchListener {

    private val mScrollbar: FastScroller
    private val mScrollPosState = ScrollPositionState()
    private var mDownX: Int = 0
    private var mDownY: Int = 0
    private var mLastY: Int = 0
    private val mScrollOffsets: SparseIntArray
    private val mScrollOffsetInvalidator: ScrollOffsetInvalidator
    private var mStateChangeListener: OnFastScrollStateChangeListener? = null

    val scrollBarWidth: Int
        get() = mScrollbar.width

    val scrollBarThumbHeight: Int
        get() = mScrollbar.thumbHeight

    private val availableScrollBarHeight: Int
        get() {
            val visibleHeight = height
            return visibleHeight - mScrollbar.thumbHeight
        }

    init {
        mScrollbar = FastScroller(context, this, attrs!!)
        mScrollOffsetInvalidator = ScrollOffsetInvalidator()
        mScrollOffsets = SparseIntArray()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addOnItemTouchListener(this)
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (getAdapter() != null) {
            getAdapter()?.unregisterAdapterDataObserver(mScrollOffsetInvalidator)
        }

        adapter?.registerAdapterDataObserver(mScrollOffsetInvalidator)

        super.setAdapter(adapter)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, ev: MotionEvent): Boolean {
        return handleTouchEvent(ev)
    }

    override fun onTouchEvent(rv: RecyclerView, ev: MotionEvent) {
        handleTouchEvent(ev)
    }

    private fun handleTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = x
                mLastY = y
                mDownY = mLastY
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener)
            }
            MotionEvent.ACTION_MOVE -> {
                mLastY = y
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener)
        }
        return mScrollbar.isDragging
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    private fun getAvailableScrollHeight(rowCount: Int, rowHeight: Int, yOffset: Int): Int {
        return getAvailableScrollHeight(rowCount * rowHeight, yOffset)
    }

    private fun getAvailableScrollHeight(adapterHeight: Int, yOffset: Int): Int {
        val visibleHeight = height
        val scrollHeight = paddingTop + yOffset + adapterHeight + paddingBottom
        return scrollHeight - visibleHeight
    }

    override fun draw(c: Canvas) {
        super.draw(c)
        onUpdateScrollbar()
        mScrollbar.draw(c)
    }

    private fun updateThumbPosition(scrollPosState: ScrollPositionState, rowCount: Int, yOffset: Int) {
        val availableScrollHeight = getAvailableScrollHeight(rowCount, scrollPosState.rowHeight, yOffset)
        val availableScrollBarHeight = availableScrollBarHeight

        if (availableScrollHeight <= 0) {
            mScrollbar.setThumbPosition(-1, -1)
            return
        }

        val scrollY = paddingTop + yOffset + scrollPosState.rowIndex * scrollPosState.rowHeight - scrollPosState.rowTopOffset
        val scrollBarY = (scrollY.toFloat() / availableScrollHeight * availableScrollBarHeight).toInt()

        val scrollBarX: Int = if (Utils.isRtl(resources)) {
            0
        } else {
            width - mScrollbar.width
        }
        mScrollbar.setThumbPosition(scrollBarX, scrollBarY)
    }

    private fun updateThumbPositionWithMeasurableAdapter(scrollPosState: ScrollPositionState, yOffset: Int) {
        val adapterHeight = calculateAdapterHeight()

        val availableScrollHeight = getAvailableScrollHeight(adapterHeight, yOffset)
        val availableScrollBarHeight = availableScrollBarHeight

        if (availableScrollHeight <= 0) {
            mScrollbar.setThumbPosition(-1, -1)
            return
        }

        val scrolledPastHeight = calculateScrollDistanceToPosition(scrollPosState.rowIndex)
        val scrollY = paddingTop + yOffset + scrolledPastHeight - scrollPosState.rowTopOffset
        val scrollBarY = (scrollY.toFloat() / availableScrollHeight * availableScrollBarHeight).toInt()

        val scrollBarX: Int = if (Utils.isRtl(resources)) {
            0
        } else {
            width - mScrollbar.width
        }
        mScrollbar.setThumbPosition(scrollBarX, scrollBarY)
    }

    fun scrollToPositionAtProgress(touchFraction: Float): String {
        val itemCount = adapter?.itemCount!!
        if (itemCount == 0) {
            return ""
        }
        var spanCount = 1
        var rowCount = itemCount
        if (layoutManager is GridLayoutManager) {
            spanCount = (layoutManager as GridLayoutManager).spanCount
            rowCount = Math.ceil(rowCount.toDouble() / spanCount).toInt()
        }

        stopScroll()

        getCurScrollState(mScrollPosState)

        val itemPos = itemCount * touchFraction

        val availableScrollHeight = getAvailableScrollHeight(rowCount, mScrollPosState.rowHeight, 0)

        val exactItemPos = (availableScrollHeight * touchFraction).toInt()

        val layoutManager = layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(spanCount * exactItemPos / mScrollPosState.rowHeight, -(exactItemPos % mScrollPosState.rowHeight))

        if (adapter !is SectionedAdapter) {
            return ""
        }

        val posInt = (if (touchFraction == 1f) itemPos - 1 else itemPos).toInt()

        val sectionedAdapter = adapter as SectionedAdapter
        return sectionedAdapter.getSectionName(posInt)
    }

    private fun onUpdateScrollbar() {

        if (adapter == null) {
            return
        }

        var rowCount = adapter?.itemCount!!
        if (layoutManager is GridLayoutManager) {
            val spanCount = (layoutManager as GridLayoutManager).spanCount
            rowCount = Math.ceil(rowCount.toDouble() / spanCount).toInt()
        }
        if (rowCount == 0) {
            mScrollbar.setThumbPosition(-1, -1)
            return
        }

        getCurScrollState(mScrollPosState)
        if (mScrollPosState.rowIndex < 0) {
            mScrollbar.setThumbPosition(-1, -1)
            return
        }

        if (adapter is MeasurableAdapter) {
            updateThumbPositionWithMeasurableAdapter(mScrollPosState, 0)
        } else {
            updateThumbPosition(mScrollPosState, rowCount, 0)
        }
    }

    private fun calculateAdapterHeight(): Int {
        return calculateScrollDistanceToPosition(adapter?.itemCount!!)
    }

    private fun calculateScrollDistanceToPosition(adapterIndex: Int): Int {
        if (mScrollOffsets.indexOfKey(adapterIndex) >= 0) {
            return mScrollOffsets.get(adapterIndex)
        }

        var totalHeight = 0
        val measurer = adapter as MeasurableAdapter

        for (i in 0 until adapterIndex) {
            mScrollOffsets.put(i, totalHeight)
            val viewType = adapter?.getItemViewType(i)!!
            totalHeight += measurer.getViewTypeHeight(this, viewType)
        }

        mScrollOffsets.put(adapterIndex, totalHeight)
        return totalHeight
    }

    private fun getCurScrollState(stateOut: ScrollPositionState) {
        stateOut.rowIndex = -1
        stateOut.rowTopOffset = -1
        stateOut.rowHeight = -1

        val itemCount = adapter?.itemCount!!

        if (itemCount == 0 || childCount == 0) {
            return
        }

        val child = getChildAt(0)

        stateOut.rowIndex = getChildAdapterPosition(child)
        if (layoutManager is GridLayoutManager) {
            stateOut.rowIndex = stateOut.rowIndex / (layoutManager as GridLayoutManager).spanCount
        }
        stateOut.rowTopOffset = layoutManager?.getDecoratedTop(child)!!
        stateOut.rowHeight = (child.height + layoutManager?.getTopDecorationHeight(child)!!
                + layoutManager?.getBottomDecorationHeight(child)!!)
    }

    fun setThumbColor(@ColorInt color: Int) {
        mScrollbar.setThumbColor(color)
    }

    fun setTrackColor(@ColorInt color: Int) {
        mScrollbar.setTrackColor(color)
    }

    fun setPopupBgColor(@ColorInt color: Int) {
        mScrollbar.setPopupBgColor(color)
    }

    fun setPopupTextColor(@ColorInt color: Int) {
        mScrollbar.setPopupTextColor(color)
    }

    fun setPopupTextSize(textSize: Int) {
        mScrollbar.setPopupTextSize(textSize)
    }

    fun setPopUpTypeface(typeface: Typeface) {
        mScrollbar.setPopupTypeface(typeface)
    }

    fun setAutoHideDelay(hideDelay: Int) {
        mScrollbar.setAutoHideDelay(hideDelay)
    }

    fun setAutoHideEnabled(autoHideEnabled: Boolean) {
        mScrollbar.setAutoHideEnabled(autoHideEnabled)
    }

    fun setStateChangeListener(stateChangeListener: OnFastScrollStateChangeListener) {
        mStateChangeListener = stateChangeListener
    }

    fun setThumbInactiveColor(autoHideEnabled: Boolean) {
        mScrollbar.setThumbInactiveColor(autoHideEnabled)
    }

    fun setPopupPosition(popupPosition: Int) {
        mScrollbar.setPopupPosition(popupPosition)
    }

    interface SectionedAdapter {
        fun getSectionName(position: Int): String
    }

    interface MeasurableAdapter {
        fun getViewTypeHeight(recyclerView: RecyclerView, viewType: Int): Int
    }

    class ScrollPositionState {
        var rowIndex: Int = 0
        var rowTopOffset: Int = 0
        var rowHeight: Int = 0
    }

    private inner class ScrollOffsetInvalidator : RecyclerView.AdapterDataObserver() {
        private fun invalidateAllScrollOffsets() {
            mScrollOffsets.clear()
        }

        override fun onChanged() {
            invalidateAllScrollOffsets()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            invalidateAllScrollOffsets()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            invalidateAllScrollOffsets()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            invalidateAllScrollOffsets()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            invalidateAllScrollOffsets()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            invalidateAllScrollOffsets()
        }
    }
}
