package com.globant.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.getImageByUrl(url: String) {
    Glide.with(context)
            .load(url)
            .centerCrop()
            .into(this)
}