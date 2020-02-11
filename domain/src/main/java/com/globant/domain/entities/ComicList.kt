package com.globant.domain.entities

import com.globant.domain.utils.EMPTY_STRING
import com.globant.domain.utils.ZERO

class ComicList(
        val available: Int? = ZERO,
        val collectionURI: String? = EMPTY_STRING,
        val items: MutableList<MarvelComicSummary>
)