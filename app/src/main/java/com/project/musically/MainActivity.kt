package com.project.musically

//import com.project.musically.R.id.seekBar

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.musically.R.id.footer_menu
import java.io.IOException
import java.io.Serializable


/*
TODO
create album selection - failed
inside of album selection add dynamically created buttons for each song - sort of failed
allow each button to play the track - succeeded
seekBar - succeeded
nextTrack - Succeeded
elapsed and remaining time - succeeded
 */

    class MainActivity : AppCompatActivity()  {

      //make the variables global so they can be used elsewhere
        private lateinit var playPauseButton: Button
        private lateinit var nextButton: Button
        private lateinit var prevButton: Button
       // private var musicPlayer = Player.getInstance()
       // var totalTime: Int = 0
//        private lateinit var currentSong :Song
        private lateinit var trackList: ArrayList<Song>
        private lateinit var player: MediaPlayer
        private lateinit var seekBar: SeekBar
        lateinit var fileDescriptor: AssetFileDescriptor
        lateinit var recordAnim: Animation


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Confirm", "The Screen Changed Successfully")

        playPauseButton = findViewById(R.id.playPauseButton)
        prevButton = findViewById(R.id.previous)
        nextButton = findViewById(R.id.next)

        //to solve the problem related to importing this, the XML
        //needed an ID added to its layout and then it needed to be
        //imported at the top at r.id not by r.layout
        val footerMenu = findViewById<View>(footer_menu)
        val trackListViewButton = footerMenu.findViewById<Button>(R.id.tracks)
        val albumSelectionButton = footerMenu.findViewById<Button>(R.id.albums)
        var remaining = findViewById<TextView>(R.id.remainingTime)
        var elapsed = findViewById<TextView>(R.id.elapsedTime)
        var name = findViewById<TextView>(R.id.trackName)
            val seekBar = findViewById<SeekBar>(R.id.seekBar)
            var trackNumber = 0
            val record = findViewById<ImageView>(R.id.record)


            var trackTitle = intent.getStringExtra("title")
            Log.d("did the title come through?", "The title of the song is: $trackTitle")
            name.text = trackTitle


            trackList = intent.getSerializableExtra("trackList") as ArrayList<Song>

            // Initialize the media player
            player = MediaPlayer()
            //play the next track when the song ends
            //this must come before the music starts playing so
            //the player knows what to do
            player.setOnCompletionListener {
                // Current track has finished playing, start the next track

                record.clearAnimation()
                    player.stop()
                    player.reset()
                    trackNumber++
                    Log.d("trackNumber?", "Current Track Index: $trackNumber")
                    val nextTrack = trackList[trackNumber]
                    val fileDescriptor = resources.openRawResourceFd(nextTrack.resourceId)
                    player.setDataSource(fileDescriptor)
                    player.prepareAsync()
                    name.text = nextTrack.title
                if (trackNumber == trackList.size - 1) {
                    // Reached the end of the track list, stop playback
                    player.stop()
                    player.reset()
                }
            }
            //receive the track from the trackView as an integer
            var default = R.raw.atott
            var currentTrack = intent.getIntExtra("track", default)
            Log.d("getTrack?", "currentTracks value is: $currentTrack")
            if (currentTrack != 0) {
                //add the track to the media player
                player.setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayer", "Error occurred while preparing the MediaPlayer. What: $what, Extra: $extra")
                    true
                }
                try {
                    fileDescriptor = resources.openRawResourceFd(currentTrack)
                    player.setDataSource(fileDescriptor)
                    player.prepareAsync() // Prepare the media player asynchronously
                } catch (e: IOException) {
                    Log.d("NoMusic", "Media Player could not start, No track available")
                    Toast.makeText(this@MainActivity, "Error playing song", Toast.LENGTH_SHORT).show()
                }
            } else {
                var startPlaylist = trackList[trackNumber].resourceId
                name.text = trackList[trackNumber].title
                Log.d("getTrack?", "playlist value is: $startPlaylist")
                fileDescriptor = resources.openRawResourceFd(startPlaylist)
                player.setDataSource(fileDescriptor)
                player.prepareAsync() // Prepare the media player asynchronously
                Toast.makeText(this@MainActivity, "Error playing song", Toast.LENGTH_SHORT).show()
            }

            //asynchronously start the music from loading in the music resource
            player.setOnPreparedListener {
                // Media player is prepared, start playback
                player.start()
                recordAnim = AnimationUtils.loadAnimation(this, R.anim.spin)
                record.startAnimation(recordAnim)
                // Get the total duration of the audio being played
                val totalDuration = player.duration
                // Set the maximum value of the SeekBar to the duration of the track
                seekBar.max = player.duration
                // Update the position of the SeekBar every 500 milliseconds
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        seekBar.progress = player.currentPosition
                        // Calculate the elapsed time
                        val currentPosition = player.currentPosition
                        val elapsedSeconds = currentPosition / 1000
                        val elapsedMinutes = elapsedSeconds / 60
                        val elapsedSecondsDisplay = elapsedSeconds % 60
                        val elapsedString = String.format("%02d:%02d", elapsedMinutes, elapsedSecondsDisplay)
                        // Calculate the time remaining
                        val remainingSeconds = (totalDuration - currentPosition) / 1000
                        val remainingMinutes = remainingSeconds / 60
                        val remainingSecondsDisplay = remainingSeconds % 60
                        val remainingString = String.format("-%02d:%02d", remainingMinutes, remainingSecondsDisplay)
                        // Update the elapsed time and time remaining TextViews
                        elapsed.text = elapsedString
                        remaining.text = remainingString
                        handler.postDelayed(this, 500) } }) }




        playPauseButton.setOnClickListener{
            if(player.isPlaying){
                playPauseButton.text = "Play"
                player.pause()
                record.clearAnimation()
            }
            else{
                playPauseButton.text = "Pause"
                player.start()
                record.startAnimation(recordAnim)
            }
        }

        prevButton.setOnClickListener{
            player.stop()
            player.reset()
            if (trackNumber == 0) {
                // If the first track is playing, go to the last track
                trackNumber = trackList.size - 1
                Log.d("trackNumber?", "Current Track Index: $trackNumber")
            } else {
                // Go to the previous track
                trackNumber--
                Log.d("trackNumber?", "Current Track Index: $trackNumber")
            }
            var startPlaylist = trackList[trackNumber].resourceId
            name.text = trackList[trackNumber].title
            Log.d("getTrack?", "playlist value is: $startPlaylist")
            fileDescriptor = resources.openRawResourceFd(startPlaylist)
            player.setDataSource(fileDescriptor)
            player.prepareAsync() // Prepare the media player asynchronously
        }

        nextButton.setOnClickListener{
            player.stop()
            player.reset()
            // Move to the next track in the list
            trackNumber++
            Log.d("trackNumber?", "Current Track Index: $trackNumber")
            if (trackNumber >= trackList.size) {
                trackNumber = 0
                Log.d("trackNumber?", "Current Track Index: $trackNumber")
            }
            var startPlaylist = trackList[trackNumber].resourceId
            name.text = trackList[trackNumber].title
            Log.d("getTrack?", "playlist value is: $startPlaylist")
            fileDescriptor = resources.openRawResourceFd(startPlaylist)
            player.setDataSource(fileDescriptor)
            player.prepareAsync() // Prepare the media player asynchronously
        }

        //var trackTime = findViewById<SeekBar>(R.id.seekBar)
        //trackTime.max = totalTime
//            trackTime.setOnSeekBarChangeListener(
//                object : SeekBar.OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                        if (fromUser) {
//                            musicPlayer.seekTo(progress)
//                        }
//                    }
//                    override fun onStartTrackingTouch(p0: SeekBar?) {
//                    }
//                    override fun onStopTrackingTouch(p0: SeekBar?) {
//                    }
//                }
//            )
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // This method is called when the progress level of the SeekBar has changed.
                    // You can use this to update the position of the playback.
                    if (fromUser) {
                        player.seekTo(progress)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // This method is called when the user starts touching the SeekBar.
                    // You can use this to pause the playback if needed.

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // This method is called when the user stops touching the SeekBar.
                    // You can use this to resume the playback from the new position.
                }
            })


        trackListViewButton.setOnClickListener {
            val intent = Intent(this, trackView::class.java)
            player.reset()
            startActivity(intent)
        }
        albumSelectionButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "This is a depreciated feature", Toast.LENGTH_SHORT).show()
        }

    }


        //an attempt to fix the serializable error i was getting
        inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
            else -> @Suppress("DEPRECATION") getSerializable(key) as? T
        }

        inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
        }



}


