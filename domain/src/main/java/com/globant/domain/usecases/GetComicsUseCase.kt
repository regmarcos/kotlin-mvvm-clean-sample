package com.globant.domain.usecases

import com.globant.domain.repositories.ComicRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetComicsUseCase: KoinComponent {
    private val comicRepository: ComicRepository by inject()
    operator fun invoke(id: Int)  = comicRepository.getComics(id)
}