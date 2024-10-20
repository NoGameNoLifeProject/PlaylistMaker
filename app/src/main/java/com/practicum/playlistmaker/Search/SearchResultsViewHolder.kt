package com.practicum.playlistmaker.Search

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Models.Track
import com.practicum.playlistmaker.R
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
                RoundedCorners(dpToPx(2f, itemView.context))
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

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}