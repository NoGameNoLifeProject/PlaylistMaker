package com.practicum.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistTracksAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: (Track) -> Unit
) : Adapter<PlaylistTracksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var tracks = listOf<Track>()

    fun setTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistTracksAdapter.ViewHolder {
        val binding =
            TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistTracksAdapter.ViewHolder, position: Int) {
        with(holder.binding) {
            val item = tracks[position]

            trackName.text = item.trackName
            trackArtist.text = item.artistName
            trackTime.text = item.getTrackLength()

            Glide.with(trackArtwork)
                .load(item.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(
                    RoundedCorners(dpToPx(2f, holder.itemView.context.resources.displayMetrics).toInt())
                )
                .into(trackArtwork)

            holder.itemView.setOnClickListener {
                onClick.invoke(item)
            }
            holder.itemView.setOnLongClickListener {
                onLongClick.invoke(item)
                true
            }
        }
    }


    override fun getItemCount() = tracks.size

}
