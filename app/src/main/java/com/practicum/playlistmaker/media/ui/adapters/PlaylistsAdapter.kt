package com.practicum.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks

class PlaylistsAdapter(
    private val getCountEnding: (count: Int) -> String,
    private val onClick: (PlaylistWithTracks) -> Unit
) : Adapter<PlaylistsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var playlists = listOf<PlaylistWithTracks>()

    fun setPlaylists(newPlaylists: List<PlaylistWithTracks>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsAdapter.ViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsAdapter.ViewHolder, position: Int) {
        with(holder.binding) {
            val item = playlists[position]

            name.text = item.playlist.name
            count.text = "${item.tracks.size} ${getCountEnding(item.tracks.size)}"

            if (item.playlist.cover.toString() == "null") {
                cover.setImageResource(R.drawable.track_placeholder)
            } else {
                cover.setImageURI(item.playlist.cover)
            }

            holder.itemView.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }


    override fun getItemCount() = playlists.size

}
