package com.project.musically

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

data class Folder(val name: String)

class Albums : AppCompatActivity() {

    var albums: ArrayList<Folder> = ArrayList()

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        var footerMenu = findViewById<View>(R.id.footer_menu)
        val playerViewButton = footerMenu.findViewById<Button>(R.id.player)
        val trackListViewButton = footerMenu.findViewById<Button>(R.id.tracks)
        val albumList = findViewById<RecyclerView>(R.id.albumView)

        // Specify the directory path as a string
        val directoryPath = "res/raw"
        Log.d("directory", directoryPath)

        val directory = File(directoryPath)
        if (directory.exists() && directory.isFile && directory.length() > 0) {
            // the file exists, is a file, and is not empty
            // do something with the file
        } else {
            // the file does not exist, is not a file, or is empty
            // handle this case appropriately, for example:
            Toast.makeText(this, "The specified file does not exist, is not a file, or is empty", Toast.LENGTH_SHORT).show()
        }
        val folders = (directory.listFiles()?.filter { it.isDirectory }?.map { Folder(it.name) } ?: emptyList())
        albums = folders.ifEmpty {
            ArrayList()
        } as ArrayList<Folder>

        playerViewButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            Log.d("travel", "Travelling to the musicPlayer")
            startActivity(intent)
        }
        trackListViewButton.setOnClickListener(){
            val intent = Intent(this, trackView::class.java)
            Log.d("travel", "Travelling to the trackList")
            startActivity(intent)
        }

        //add tracks to the trackList View
        albumList.layoutManager = LinearLayoutManager(this)
        albumList.adapter =  AlbumListAdapter(albums,applicationContext)
    }



}