package com.practicum.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchResultsViewHolder(itemView: View, private val onTrackClick: (Track) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val trackArtwork: ImageView = itemView.findViewById(R.id.track_artwork)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(dpToPx(2f, itemView.context.resources.displayMetrics).toInt())
            )
            .into(trackArtwork)

        trackName.text = item.trackName
        trackArtist.text = item.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        trackArtist.requestLayout()

        itemView.setOnClickListener {
            onTrackClick.invoke(item)
        }
    }
}