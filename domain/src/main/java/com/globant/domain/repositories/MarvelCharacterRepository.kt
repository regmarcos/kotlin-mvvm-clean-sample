package com.globant.domain.repositories

import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.utils.Result

interface MarvelCharacterRepository {
    fun getCharacterById(id: Int, getFromRemote: Boolean): Result<MarvelCharacter>
    fun getCharacters(): Result<List<MarvelCharacter>>
    fun getCharactersFromDB(): Result<List<MarvelCharacter>>
}
