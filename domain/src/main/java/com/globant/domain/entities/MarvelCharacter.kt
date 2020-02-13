package com.globant.domain.entities

import com.globant.domain.utils.DEFAULT_ID
import com.globant.domain.utils.NOT_FOUND


class MarvelCharacter(
        val id: Int = DEFAULT_ID,
        val name: String = NOT_FOUND,
        val description: String = NOT_FOUND,
        val thumbnail: MarvelThumbnail?,
        val comics: ComicList
)
