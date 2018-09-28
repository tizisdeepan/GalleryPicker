package com.picker.gallery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryImage (
        var ID: String? = null,
        var DATA: String? = null,
        var DATE_ADDED: String? = null,
        var DATE_MODIFIED: String? = null,
        var DISPLAY_NAME: String? = null,
        var HEIGHT: String? = null,
        var MIME_TYPE: String? = null,
        var SIZE: String? = null,
        var TITLE: String? = null,
        var WIDTH: String? = null,

        var BUCKET_DISPLAY_NAME: String? = null,
        var BUCKET_ID: String? = null,
        var DATE_TAKEN: String? = null,
        var DESCRIPTION: String? = null,
        var IS_PRIVATE: String? = null,
        var LATITUDE: String? = null,
        var LONGITUDE: String? = null,
        var MINI_THUMB_MAGIC: String? = null,
        var ORIENTATION: String? = null,
        var PICASA_ID: String? = null,

        var ALBUM_NAME: String? = null
): Parcelable