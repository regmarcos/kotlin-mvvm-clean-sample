package com.globant.adapter.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.globant.domain.entities.Comic
import com.globant.extensions.getImageByUrl
import com.globant.listener.ComicListener
import com.globant.utils.DOT
import kotlinx.android.synthetic.main.comic_card_view.view.thumbnail_comic_card_view

class ComicAdapterViewHolder(val view: View, val listener: ComicListener): RecyclerView.ViewHolder(view) {
    fun bind(comic: Comic) = with(itemView) {
        val url = "${comic.thumbnail?.path}$DOT${comic.thumbnail?.extension}"
        thumbnail_comic_card_view.getImageByUrl(url)
        Log.d("comicsURL", url)
        setOnClickListener { listener(comic) }
    }
}