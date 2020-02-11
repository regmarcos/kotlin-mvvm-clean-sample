package com.globant.data.repositories

import com.globant.data.database.ComicDatabase
import com.globant.data.service.ComicService
import com.globant.domain.entities.Comic
import com.globant.domain.repositories.ComicRepository
import com.globant.domain.utils.Result

class ComicRepositoryImpl(
        private val comicService: ComicService = ComicService(),
        private val comicDatabase: ComicDatabase = ComicDatabase()
):ComicRepository {

    override fun getComics(characterId: Int): Result<List<Comic>> {
        val comicResult = comicService.getComics(characterId)
        if (comicResult is Result.Success) {
            comicResult.data.map { result -> insertOrUpdateComic(result) }
        }
        return comicResult
    }

    private fun insertOrUpdateComic(comic: Comic) {
        comicDatabase.insertOrUpdateComic(comic)
    }
}