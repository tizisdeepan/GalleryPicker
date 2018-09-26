package com.picker.gallery.model

import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryData(var id: Int = 0, var albumName: String = "", var photoUri: String = "", var albumId:Int = 0, var isSelected: Boolean = false, var isEnabled: Boolean = true, var mediaType: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, var duration: Int = 0, var dateAdded: String = "", var thumbnail: String = ""): Parcelable