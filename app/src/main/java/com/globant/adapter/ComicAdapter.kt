package com.globant.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globant.adapter.viewholder.ComicAdapterViewHolder
import com.globant.domain.entities.Comic
import com.globant.extensions.inflate
import com.globant.listener.ComicListener
import com.globant.myapplication.R
import kotlin.properties.Delegates

class ComicAdapter(data: List<Comic> = emptyList(), val listener: ComicListener) : RecyclerView.Adapter<ComicAdapterViewHolder>() {

    var data by Delegates.observable(data) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicAdapterViewHolder = ComicAdapterViewHolder(parent.inflate(R.layout.comic_card_view), listener)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ComicAdapterViewHolder, position: Int) {
        holder.bind(data[position])
    }
}