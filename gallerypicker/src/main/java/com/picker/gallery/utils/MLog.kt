package com.picker.gallery.utils

import android.util.Log

/**
 * Created by deepan-5901 on 03/04/18.
 */
object MLog {
    var canLog = true
    fun e(tag: String, message: String?) {
        if (canLog) Log.e(tag, message)
    }

    fun d(tag: String, message: String?) {
        if (canLog) Log.d(tag, message)
    }

    fun v(tag: String, message: String?) {
        if (canLog) Log.v(tag, message)
    }
}