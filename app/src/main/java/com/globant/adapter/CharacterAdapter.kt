package com.globant.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globant.adapter.viewholder.CharacterAdapterViewHolder
import com.globant.domain.entities.MarvelCharacter
import com.globant.extensions.inflate
import com.globant.listener.CharacterListener
import com.globant.myapplication.R
import kotlin.properties.Delegates

class CharacterAdapter(data: List<MarvelCharacter> = emptyList(), val listener: CharacterListener) : RecyclerView.Adapter<CharacterAdapterViewHolder>() {

    var data by Delegates.observable(data) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterAdapterViewHolder = CharacterAdapterViewHolder(parent.inflate(R.layout.adapter_card_view_characters), listener)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CharacterAdapterViewHolder, position: Int) {
        holder.bind(data[position])
    }
}