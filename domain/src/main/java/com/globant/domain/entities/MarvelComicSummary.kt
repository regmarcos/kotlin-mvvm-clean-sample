package com.globant.domain.entities

import com.globant.domain.utils.NOT_FOUND
import com.globant.domain.utils.ZERO

class MarvelComicSummary(
        val id: Int? = ZERO,
        val title: String? = NOT_FOUND,
        val description: String? = NOT_FOUND
)