package com.picker.gallery.utils.keypad;

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.content.Context
import android.view.inputmethod.InputMethodManager

import java.util.HashMap

class KeyboardUtils private constructor(act: Activity, private var mCallback: SoftKeyboardToggleListener?) : ViewTreeObserver.OnGlobalLayoutListener {
    private val mRootView: View
    private var prevValue: Boolean? = null
    private var mScreenDensity = 1f

    interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)

        val heightDiff = mRootView.rootView.height - (r.bottom - r.top)
        val dp = heightDiff / mScreenDensity
        val isVisible = dp > MAGIC_NUMBER

        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible
            mCallback!!.onToggleSoftKeyboard(isVisible)
        }
    }

    private fun removeListener() {
        mCallback = null
        mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    init {
        mRootView = (act.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mScreenDensity = act.resources.displayMetrics.density
    }

    companion object {
        private val MAGIC_NUMBER = 200
        private val sListenerMap = HashMap<SoftKeyboardToggleListener, KeyboardUtils>()

        fun addKeyboardToggleListener(act: Activity, listener: SoftKeyboardToggleListener) {
            removeKeyboardToggleListener(listener)
            sListenerMap.put(listener, KeyboardUtils(act, listener))
        }

        fun removeKeyboardToggleListener(listener: SoftKeyboardToggleListener) {
            if (sListenerMap.containsKey(listener)) {
                val k = sListenerMap[listener]
                k?.removeListener()
                sListenerMap.remove(listener)
            }
        }

        fun removeAllKeyboardToggleListeners() {
            for (l in sListenerMap.keys)
                sListenerMap[l]?.removeListener()
            sListenerMap.clear()
        }

        fun toggleKeyboardVisibility(context: Context) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun forceCloseKeyboard(activeView: View) {
            val inputMethodManager = activeView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activeView.windowToken, 0)
        }
    }

}