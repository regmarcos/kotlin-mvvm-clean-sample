package com.globant.data.mapper

import com.globant.data.database.entity.ComicRealm
import com.globant.data.database.entity.MarvelThumbnailRealm
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelThumbnail

class ComicMapperLocal : BaseMapperRepository<ComicRealm, Comic> {
    override fun transform(type: ComicRealm): Comic = Comic(
            type.id,
            type.title,
            transformThumbnail(type.thumbnail)
    )

    override fun transformToRepository(type: Comic): ComicRealm = ComicRealm(
            type.id,
            type.title,
            transformToThumbnailRepository(type.thumbnail)
    )

    private fun transformThumbnail(type: MarvelThumbnailRealm?): MarvelThumbnail = MarvelThumbnail(
            type?.path,
            type?.extension
    )

    private fun transformToThumbnailRepository(type: MarvelThumbnail?): MarvelThumbnailRealm = MarvelThumbnailRealm(
            type?.path,
            type?.extension
    )
}