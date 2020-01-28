package com.globant.data.mapper

import com.globant.data.service.response.CharacterResponse
import com.globant.data.service.response.ThumbnailResponse
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.entities.MarvelThumbnail

open class CharacterMapperService : BaseMapperRepository<CharacterResponse, MarvelCharacter> {

    override fun transform(type: CharacterResponse): MarvelCharacter =
            MarvelCharacter(
                    type.id,
                    type.name,
                    type.description,
                    transformThumbnail(type.thumbnail)
            )

    override fun transformToRepository(type: MarvelCharacter): CharacterResponse =
            CharacterResponse(
                    type.id,
                    type.name,
                    type.description,
                    transformThumbnailToRepository(type.thumbnail)
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
}
