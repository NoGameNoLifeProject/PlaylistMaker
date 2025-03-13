package com.practicum.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.models.Track

class FavoritesAdapter(private val onClick: (Track) -> Unit) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var tracks = mutableListOf<Track>()

    fun setTracks(newTracks: List<Track>) {
        tracks = newTracks.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = tracks[position]

            trackArtist.text = item.artistName
            trackName.text = item.trackName
            trackTime.text = item.getTrackLength()

            Glide.with(trackArtwork)
                .load(item.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        dpToPx(
                            2f,
                            holder.itemView.context.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(trackArtwork)

            holder.itemView.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = tracks.size
}