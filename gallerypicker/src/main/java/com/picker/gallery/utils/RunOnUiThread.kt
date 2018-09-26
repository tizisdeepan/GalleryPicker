package com.picker.gallery.utils

import android.content.Context
import com.picker.gallery.utils.MLog
import org.jetbrains.anko.runOnUiThread

/**
 * Created by deepan-5901 on 23/03/18.
 */
class RunOnUiThread(var context: Context?) {
    fun safely(dothis: () -> Unit) {
        context?.runOnUiThread {
            try {
                dothis.invoke()
            } catch (e: Exception) {
                MLog.e("runonui", e.toString())
                e.printStackTrace()
            }
        }
    }
}