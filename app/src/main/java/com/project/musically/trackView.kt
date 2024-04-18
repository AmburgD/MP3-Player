package com.project.musically


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.musically.R.id.footer_menu
import kotlinx.android.parcel.Parcelize


@Parcelize
class Song(var resourceId: Int , var title: String, var album: String, var artist: String) :Parcelable


class trackView : AppCompatActivity() {

    var tracks: ArrayList<Song> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_view)

        val footerMenu = findViewById<View>(footer_menu)
        val albumSelectionButton = footerMenu.findViewById<Button>(R.id.albums)
        val playerViewButton = footerMenu.findViewById<Button>(R.id.player)
        val trackList = findViewById<RecyclerView>(R.id.songListView)

        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val requestCode = 100


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permissions have already been granted
        } else {
            // Request permissions from the user
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }

//        val projection = arrayOf(
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.DURATION
//        )
//        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
//        val cursor = contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            selection,
//            null,
//            null
//        )
//        while (cursor?.moveToNext() == true) {
//            val songData = Song(
//                cursor.getString(1),
//                cursor.getString(0),
//                cursor.getString(2)
//            )
//            if (File(songData.path).exists()) {
//                tracks.add(songData)
//            }
//        }
//        cursor?.close()


        val track1 = R.raw.lmbyh
        val track2 = R.raw.time
        val track3 = R.raw.ngba
        val track4 = R.raw.atott


        val hog = Song(track1, "Let Me Be Your Hog", "UHF", "Weird Al Yankovic")
        val time = Song(track2, "Time", "Dark Side of the Moon", "Pink Floyd")
        val mac = Song(track3, "Never Going Back Again", "Rumors", "Fleetwood Mac")
        val tail = Song(track4, "A Trick Of The Tail", "A Trick Of The Tail", "Genesis")


        tracks.add(hog)
        tracks.add(time)
        tracks.add(mac)
        tracks.add(tail)

        //add tracks to the trackList View
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = TrackListAdapter(tracks,applicationContext)

        playerViewButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("trackList", tracks)
            startActivity(intent)
        }

        albumSelectionButton.setOnClickListener {
            Toast.makeText(this@trackView, "This is a depreciated feature", Toast.LENGTH_SHORT).show()
        }

    }
}

//    fun addTracksViaFolder(directory: String): ArrayList<File> {
//
//        var tracklist = arrayListOf<File>()
//
//        val dir = File(directory)
//
//        if (dir.isDirectory) {
//            // get a list of all files in the directory with the .mp3 extension
//            val files = dir.listFiles { file -> file.extension == "mp3" || file.extension == ".wav" }
//            if (files != null) {
//                // add the files to the ArrayList
//                tracklist.addAll(files)
//            }
//        }
//
//        return tracklist
//    }
