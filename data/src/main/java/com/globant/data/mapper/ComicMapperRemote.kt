package com.globant.data.mapper

import com.globant.data.service.response.ComicResponse
import com.globant.data.service.response.ThumbnailResponse
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelThumbnail

class ComicMapperRemote : BaseMapperRepository<ComicResponse, Comic> {

    override fun transform(type: ComicResponse): Comic = Comic(
            type.id,
            type.title,
            transformThumbnail(type.thumbnail)
    )

    override fun transformToRepository(type: Comic): ComicResponse = ComicResponse(
            type.id,
            type.title,
            transformThumbnailToRepository(type.thumbnail)
    )

    private fun transformThumbnail(type: ThumbnailResponse): MarvelThumbnail? =
            MarvelThumbnail(
                    type.path,
                    type.extension
            )

    private fun transformThumbnailToRepository(type: MarvelThumbnail?): ThumbnailResponse =
            ThumbnailResponse(
                    type?.path,
                    type?.extension
            )
}