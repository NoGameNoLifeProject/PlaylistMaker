package com.practicum.playlistmaker.Search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Models.Track
import com.practicum.playlistmaker.R

class SearchResultsAdapter(private val data: List<Track>, private val onTrackClick: (Track) -> Unit): RecyclerView.Adapter<SearchResultsViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchResultsViewHolder(view) {
            onTrackClick(it)
        }
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            onTrackClick(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}