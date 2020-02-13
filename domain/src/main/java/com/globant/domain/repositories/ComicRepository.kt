package com.globant.domain.repositories

import com.globant.domain.utils.Result
import com.globant.domain.entities.Comic

interface ComicRepository {
    fun getComics(characterId: Int): Result<List<Comic>>
}