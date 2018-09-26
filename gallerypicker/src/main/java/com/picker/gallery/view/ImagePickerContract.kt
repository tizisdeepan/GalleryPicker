package com.picker.gallery.view

import android.content.Context
import com.picker.gallery.model.GalleryAlbums
import com.picker.gallery.model.GalleryData
import kotlin.collections.ArrayList

interface ImagePickerContract {
    fun initRecyclerViews()
    fun galleryOperation()
    fun toggleDropdown()
    fun getPhoneAlbums(context: Context, listener: OnPhoneImagesObtained)
    fun updateTitle(galleryAlbums: GalleryAlbums = GalleryAlbums())
    fun updateSelectedPhotos(selectedlist: ArrayList<GalleryData> = ArrayList())
}