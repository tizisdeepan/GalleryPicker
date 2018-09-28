package com.picker.gallery.model.interactor

import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import com.picker.gallery.model.GalleryImage
import com.picker.gallery.model.GalleryVideo
import java.io.File
import kotlin.collections.ArrayList

class GalleryPicker(var ctx: Context) {

    fun getImages(): ArrayList<GalleryImage> {
        val images: ArrayList<GalleryImage> = ArrayList()
        try {
            if (isReadWritePermitted()) {
                val imagesProjection = arrayOf(MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.DATE_MODIFIED,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.HEIGHT,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.WIDTH,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.DESCRIPTION,
                        MediaStore.Images.Media.IS_PRIVATE,
                        MediaStore.Images.Media.LATITUDE,
                        MediaStore.Images.Media.LONGITUDE,
                        MediaStore.Images.Media.MINI_THUMB_MAGIC,
                        MediaStore.Images.Media.ORIENTATION,
                        MediaStore.Images.Media.PICASA_ID)
                val imagesQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val imagescursor = ctx.contentResolver.query(imagesQueryUri, imagesProjection, null, null, null)

                if (imagescursor != null && imagescursor.count > 0) {
                    if (imagescursor.moveToFirst()) {
                        do {
                            val image = GalleryImage()
                            image.ID = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media._ID))
                            image.DATA = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATA))
                            image.DATE_ADDED = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                            image.DATE_MODIFIED = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                            image.DISPLAY_NAME = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                            image.HEIGHT = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                            image.MIME_TYPE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                            image.SIZE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                            image.TITLE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.TITLE))
                            image.WIDTH = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                            image.BUCKET_DISPLAY_NAME = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                            image.BUCKET_ID = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                            image.DATE_TAKEN = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                            image.DESCRIPTION = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION))
                            image.IS_PRIVATE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE))
                            image.LATITUDE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.LATITUDE))
                            image.LONGITUDE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE))
                            image.MINI_THUMB_MAGIC = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC))
                            image.ORIENTATION = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
                            image.PICASA_ID = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.PICASA_ID))
                            image.ALBUM_NAME = File(image.DATA).parentFile.name
                            images.add(image)
                        } while (imagescursor.moveToNext())
                    }
                    imagescursor.close()
                }
            }
        } catch (e: Exception) {
            Log.e("IMAGES", e.toString())
        } finally {
            return images
        }
    }

    fun getVideos(): ArrayList<GalleryVideo> {
        val videos: ArrayList<GalleryVideo> = ArrayList()
        try {
            if (isReadWritePermitted()) {
                val videoProjection = arrayOf(MediaStore.Images.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DATE_ADDED,
                        MediaStore.Video.Media.DATE_MODIFIED,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.HEIGHT,
                        MediaStore.Video.Media.MIME_TYPE,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.WIDTH,
                        MediaStore.Video.Media.ALBUM,
                        MediaStore.Video.Media.ARTIST,
                        MediaStore.Video.Media.BOOKMARK,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.BUCKET_ID,
                        MediaStore.Video.Media.CATEGORY,
                        MediaStore.Video.Media.DATE_TAKEN,
                        MediaStore.Video.Media.DESCRIPTION,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.IS_PRIVATE,
                        MediaStore.Video.Media.LANGUAGE,
                        MediaStore.Video.Media.LATITUDE,
                        MediaStore.Video.Media.LONGITUDE,
                        MediaStore.Video.Media.MINI_THUMB_MAGIC,
                        MediaStore.Video.Media.RESOLUTION,
                        MediaStore.Video.Media.TAGS)

                val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

                val videocursor = ctx.contentResolver.query(videoQueryUri, videoProjection, null, null, null)

                if (videocursor != null && videocursor.count > 0) {
                    if (videocursor.moveToFirst()) {
                        do {
                            val video = GalleryVideo()
                            video.ID = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media._ID))
                            video.DATA = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATA))
                            video.DATE_ADDED = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                            video.DATE_MODIFIED = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                            video.DISPLAY_NAME = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                            video.HEIGHT = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.HEIGHT))
                            video.MIME_TYPE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
                            video.SIZE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                            video.TITLE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                            video.WIDTH = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.WIDTH))
                            video.ALBUM = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.ALBUM))
                            video.ARTIST = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.ARTIST))
                            video.BOOKMARK = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK))
                            video.BUCKET_DISPLAY_NAME = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                            video.BUCKET_ID = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                            video.CATEGORY = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.CATEGORY))
                            video.DATE_TAKEN = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN))
                            video.DESCRIPTION = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION))
                            video.DURATION = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                            video.IS_PRIVATE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE))
                            video.LANGUAGE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.LANGUAGE))
                            video.LATITUDE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.LATITUDE))
                            video.LONGITUDE = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE))
                            video.MINI_THUMB_MAGIC = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC))
                            video.RESOLUTION = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION))
                            video.TAGS = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.TAGS))
                            video.ALBUM_NAME = File(video.DATA).parentFile.name
                            videos.add(video)
                        } while (videocursor.moveToNext())
                    }
                    videocursor.close()
                }
            }
        } catch (e: Exception) {
            Log.e("VIDEOS", e.toString())
        } finally {
            return videos
        }
    }

    private fun isReadWritePermitted(): Boolean = (ctx.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ctx.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}