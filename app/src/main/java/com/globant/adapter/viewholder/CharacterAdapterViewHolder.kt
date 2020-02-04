package com.globant.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.globant.domain.entities.MarvelCharacter
import com.globant.extensions.getImageByUrl
import com.globant.listener.CharacterListener
import com.globant.utils.DOT
import kotlinx.android.synthetic.main.adapter_card_view_characters.view.card_view_character_name
import kotlinx.android.synthetic.main.adapter_card_view_characters.view.card_view_character_thumbnail

class CharacterAdapterViewHolder(val view: View, val listener: CharacterListener): RecyclerView.ViewHolder(view) {
    fun bind(character: MarvelCharacter) = with(itemView){
        card_view_character_name.text = character.name
        val string = "${character.thumbnail?.path}$DOT${character.thumbnail?.extension}"
        string.let { card_view_character_thumbnail.getImageByUrl(it)}
        setOnClickListener { listener(character) }
    }
}