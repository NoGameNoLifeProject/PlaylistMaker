package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class SearchResultsAdapter(private val onTrackClick: (Track) -> Unit): RecyclerView.Adapter<SearchResultsViewHolder> () {
    private var data: List<Track> = listOf()

    fun updateData(newData: List<Track>) {
        data = newData
        notifyDataSetChanged()
    }

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