package com.globant.domain.usecases

import com.globant.domain.repositories.MarvelCharacterRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetCharactersUseCase: KoinComponent {
    private val marvelCharacterRepository : MarvelCharacterRepository by inject()
    operator fun invoke() = marvelCharacterRepository.getCharacters()
}