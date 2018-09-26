package com.picker.gallery.utils.scroll

/**
 * Created by deepan-5901 on 25/01/18.
 */
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View

object Utils {
    fun toPixels(res: Resources, dp: Float): Int {
        return (dp * res.displayMetrics.density).toInt()
    }

    fun toScreenPixels(res: Resources, sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, res.displayMetrics).toInt()
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun isRtl(res: Resources): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && res.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }
}