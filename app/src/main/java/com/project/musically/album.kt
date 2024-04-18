package com.project.musically

import android.widget.Button
import android.widget.LinearLayout
import java.io.File

class Album( title:String, band:String, yearPublished:Int) {

    private val tracklist = ArrayList<File>()

    /**
     * add tracks to an album via a directory string
     */
    fun addTracksViaFolder(directory: String){

        val dir = File(directory)

        if (dir.isDirectory) {
            // get a list of all files in the directory with the .mp3 extension
            val files = dir.listFiles { file -> file.extension == "mp3" || file.extension == ".wav" }
            if (files != null) {
                // add the files to the ArrayList
                tracklist.addAll(files)
            }
        }
    }

    /**
     * This function allows the user to select a track to start playing from the list in the album
     */
    fun getTrack(trackNumber:Int): File {

        return tracklist[trackNumber-1]
    }

    /**
     * This function allows you to get the name of the track that can be displayed
     */
    fun getTrackName(): ArrayList<File> {
        return tracklist
    }

    fun displayAlbum(){
    }


}
