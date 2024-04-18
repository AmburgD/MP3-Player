package com.project.musically

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.musically.R.id.Album
import com.project.musically.R.id.Artist
import com.project.musically.R.id.Title



class TrackListAdapter(private val trackList: ArrayList<Song>, private val context: Context) :
    RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(Title)
        val artist: TextView = itemView.findViewById(Artist)
        val album: TextView = itemView.findViewById(Album)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tracklist_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = trackList[position]

        holder.title.text = songData.title
        holder.artist.text = songData.artist
        holder.album.text = songData.album

        Log.d("Number Of Tracks", "NumTracks = $itemCount")

        holder.itemView.setOnClickListener { //navigate to another activity
//              var musicPlayer = MediaPlayer()
//                musicPlayer.reset();
//            Player.currentIndex = position

            val intent = Intent(context, AlternateMain::class.java)
            Player.getInstance()
            intent.putExtra("track", songData.resourceId)
            intent.putExtra("title", songData.title)
            // Put the selected song index as extra in the intent
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            Log.d("Changing?", "Did the screen Change?")
            context.startActivity(intent)
        }
    }

}
