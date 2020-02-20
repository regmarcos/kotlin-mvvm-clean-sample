package com.globant.data.repositories

import com.globant.data.database.CharacterDatabase
import com.globant.data.service.CharacterService
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.repositories.MarvelCharacterRepository
import com.globant.domain.utils.Result

class MarvelCharacterRepositoryImpl(
    private val characterService: CharacterService,
    private val characterDatabase: CharacterDatabase
) : MarvelCharacterRepository {

    override fun getCharacterById(id: Int, getFromRemote: Boolean): Result<MarvelCharacter> =
        if (getFromRemote) {
            val marvelCharacterResult = characterService.getCharacterById(id)
            if (marvelCharacterResult is Result.Success) {
                insertOrUpdateCharacter(marvelCharacterResult.data)
            }
            marvelCharacterResult
        } else {
            characterDatabase.getCharacterById(id)
        }

    override fun getCharacters(): Result<List<MarvelCharacter>> {
        val marvelCharacterResult = characterService.getCharacters()
        if (marvelCharacterResult is Result.Success) {
            marvelCharacterResult.data.map { characterResponse -> insertOrUpdateCharacter(characterResponse) }
        }
        return marvelCharacterResult
    }

    override fun getCharactersFromDB(): Result<List<MarvelCharacter>> {
        return characterDatabase.getAllCharacters()
    }

    private fun insertOrUpdateCharacter(character: MarvelCharacter) {
        characterDatabase.insertOrUpdateCharacter(character)
    }
}
