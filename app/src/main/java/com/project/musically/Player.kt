package com.project.musically

import android.media.MediaPlayer


object Player {
    private var instance: MediaPlayer? = null

    @JvmName("getPlayerInstance")
    fun getInstance(): MediaPlayer? {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance
    }

    var currentIndex = 0
}