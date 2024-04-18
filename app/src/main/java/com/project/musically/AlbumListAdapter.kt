package com.project.musically

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlbumListAdapter (private val folders: List<Folder>, private val context: Context) : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        holder.name.text = folder.name
        holder.itemView.setOnClickListener { //navigate to another activity
//

            val intent = Intent(context, trackView::class.java)
            // Put the selected song index as extra in the intent
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            Log.d("Changing?", "Did the screen Change?")
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = folders.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.albumID)
    }
}