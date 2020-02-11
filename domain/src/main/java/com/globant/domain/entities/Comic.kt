package com.globant.domain.entities

import com.globant.domain.utils.EMPTY_STRING
import com.globant.domain.utils.ZERO

class Comic (
        val id: Int = ZERO,
        val title: String = EMPTY_STRING,
        val thumbnail: MarvelThumbnail?
)