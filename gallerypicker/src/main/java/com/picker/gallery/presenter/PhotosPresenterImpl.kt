package com.picker.gallery.presenter

import com.picker.gallery.model.interactor.PhotosInteractorImpl
import com.picker.gallery.view.PhotosFragment

class PhotosPresenterImpl(var photosFragment: PhotosFragment): PhotosPresenter {
    val interactor: PhotosInteractorImpl = PhotosInteractorImpl(this)
    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }
}