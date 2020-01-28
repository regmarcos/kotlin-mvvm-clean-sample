package com.globant.data.mapper

import com.globant.data.database.entity.MarvelCharacterRealm
import com.globant.data.database.entity.MarvelThumbnailRealm
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.entities.MarvelThumbnail

class CharacterMapperLocal : BaseMapperRepository<MarvelCharacterRealm, MarvelCharacter> {

    override fun transform(type: MarvelCharacterRealm): MarvelCharacter = MarvelCharacter(
            type.id,
            type.name,
            type.description,
            transformThumbnail(type.thumbnail)
    )

    override fun transformToRepository(type: MarvelCharacter): MarvelCharacterRealm = MarvelCharacterRealm(
            type.id,
            type.name,
            type.description,
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
