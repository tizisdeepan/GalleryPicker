package com.picker.gallery.view

import com.picker.gallery.model.GalleryAlbums
import kotlin.collections.ArrayList

interface OnPhoneImagesObtained {
    fun onComplete(albums: ArrayList<GalleryAlbums>)
    fun onError()
}
