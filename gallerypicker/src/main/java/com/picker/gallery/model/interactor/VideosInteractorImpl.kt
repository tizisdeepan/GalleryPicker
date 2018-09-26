package com.picker.gallery.model.interactor

import android.provider.MediaStore
import com.picker.gallery.model.GalleryAlbums
import com.picker.gallery.model.GalleryData
import com.picker.gallery.presenter.VideosPresenterImpl
import com.picker.gallery.utils.MLog
import java.io.File

class VideosInteractorImpl(var presenter: VideosPresenterImpl) : VideosInteractor {

    override fun getPhoneAlbums() {
        val galleryAlbums: ArrayList<GalleryAlbums> = ArrayList()
        val albumsNames: ArrayList<String> = ArrayList()

        val videoProjection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION)

        val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val videocursor = presenter.videosFragment.ctx.contentResolver.query(videoQueryUri, videoProjection, null, null, null)

        MLog.e("VIDEOS", videocursor.count.toString())

        try {
            if (videocursor != null && videocursor.count > 0) {
                if (videocursor.moveToFirst()) {
                    val idColumn = videocursor.getColumnIndex(MediaStore.Video.Media._ID)
                    val dataColumn = videocursor.getColumnIndex(MediaStore.Video.Media.DATA)
                    val dateAddedColumn = videocursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                    val titleColumn = videocursor.getColumnIndex(MediaStore.Video.Media.TITLE)
                    val durationColumn = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                    do {
                        val id = videocursor.getString(idColumn)
                        val data = videocursor.getString(dataColumn)
                        val dateAdded = videocursor.getString(dateAddedColumn)
                        val title = videocursor.getString(titleColumn)
                        val duration = videocursor.getInt(durationColumn)
                        val galleryData = GalleryData()
                        galleryData.albumName = File(data).parentFile.name
                        galleryData.photoUri = data
                        galleryData.id = Integer.valueOf(id)
                        galleryData.duration = duration
                        galleryData.mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                        galleryData.dateAdded = dateAdded
                        if (albumsNames.contains(galleryData.albumName)) {
                            for (album in galleryAlbums) {
                                if (album.name == galleryData.albumName) {
                                    galleryData.albumId = album.id
                                    album.albumPhotos.add(galleryData)
                                    presenter.videosFragment.photoList.add(galleryData)
                                    break
                                }
                            }
                        } else {
                            val album = GalleryAlbums()
                            album.id = galleryData.id
                            galleryData.albumId = galleryData.id
                            album.name = galleryData.albumName
                            album.coverUri = galleryData.photoUri
                            album.albumPhotos.add(galleryData)
                            presenter.videosFragment.photoList.add(galleryData)
                            galleryAlbums.add(album)
                            albumsNames.add(galleryData.albumName)
                        }
                    } while (videocursor.moveToNext())
                }
                videocursor.close()
            } else presenter.videosFragment.listener.onError()
        } catch (e: Exception) {
            MLog.e("IMAGE PICKER", e.toString())
        } finally {
            presenter.videosFragment.listener.onComplete(galleryAlbums)
        }
    }
}