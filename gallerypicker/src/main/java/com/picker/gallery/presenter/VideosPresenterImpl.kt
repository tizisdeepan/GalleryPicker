package com.picker.gallery.presenter

import com.picker.gallery.model.interactor.VideosInteractorImpl
import com.picker.gallery.view.VideosFragment

class VideosPresenterImpl(var videosFragment: VideosFragment): VideosPresenter {
    var interactor = VideosInteractorImpl(this)
    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }
}