package com.picker.gallerysample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import com.picker.gallery.GalleryPicker

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_READ_WRITE = 123

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(isReadWritePermitted()) {
            Log.e("HERE", "HERE")
            getGalleryResults()
        } else {
            Log.e("HERE1", "HERE1")
            checkReadWritePermission()
        }
    }

    fun getGalleryResults(){
        val images = GalleryPicker(this).getImages()
        Log.e("IMAGES", images.size.toString())
        val videos = GalleryPicker(this).getVideos()
        Log.e("VIDEOS", videos.size.toString())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadWritePermission(): Boolean {
        Log.e("HERE2", "HERE2")
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_READ_WRITE)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.e("HERE3", "HERE3")
        when (requestCode) {
            PERMISSIONS_READ_WRITE -> {
                Log.e("HERE4", "HERE4")
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("HERE5", "HERE5")
                    getGalleryResults()
                }
            }
        }
    }

    private fun isReadWritePermitted(): Boolean = (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}
