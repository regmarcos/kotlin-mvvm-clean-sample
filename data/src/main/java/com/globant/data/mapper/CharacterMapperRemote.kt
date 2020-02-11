package com.globant.data.mapper

import com.globant.data.service.response.CharacterResponse
import com.globant.data.service.response.ComicListResponse
import com.globant.data.service.response.MarvelComicResponse
import com.globant.data.service.response.ThumbnailResponse
import com.globant.domain.entities.ComicList
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.entities.MarvelComicSummary
import com.globant.domain.entities.MarvelThumbnail

open class CharacterMapperRemote : BaseMapperRepository<CharacterResponse, MarvelCharacter> {

    override fun transform(type: CharacterResponse): MarvelCharacter =
            MarvelCharacter(
                    type.id,
                    type.name,
                    type.description,
                    transformThumbnail(type.thumbnail),
                    transformComicList(type.comics)
            )

    override fun transformToRepository(type: MarvelCharacter): CharacterResponse =
            CharacterResponse(
                    type.id,
                    type.name,
                    type.description,
                    transformThumbnailToRepository(type.thumbnail),
                    transformComicListToRepository(type.comics)
            )
    private fun transformThumbnail(type: ThumbnailResponse): MarvelThumbnail?=
            MarvelThumbnail(
                    type.path,
                    type.extension
            )
    private fun transformThumbnailToRepository(type: MarvelThumbnail?): ThumbnailResponse =
            ThumbnailResponse(
                    type?.path,
                    type?.extension
            )
    private fun transformComic(type: MarvelComicResponse): MarvelComicSummary = MarvelComicSummary(
            type.id,
            type.title,
            type.description
    )

    private fun transformComicToRepository(type: MarvelComicSummary?): MarvelComicResponse = MarvelComicResponse(
            type?.id,
            type?.title,
            type?.description
    )

    private fun transformComicListToRepository(type: ComicList?): ComicListResponse {
        val realmList = mutableListOf<MarvelComicResponse>()
        type?.items?.forEach {
            realmList.add(transformComicToRepository(it))
        }
        return ComicListResponse(type?.available, type?.collectionURI, realmList)
    }

    private fun transformComicList(type: ComicListResponse): ComicList {
        val realmList = mutableListOf<MarvelComicSummary>()
        type.items.forEach {
            realmList.add(transformComic(it))
        }
        return ComicList(type.available, type.collectionURI, realmList)
    }
}