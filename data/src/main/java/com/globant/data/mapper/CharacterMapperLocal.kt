package com.globant.data.mapper

import com.globant.data.database.entity.ComicListRealm
import com.globant.data.database.entity.MarvelCharacterRealm
import com.globant.data.database.entity.MarvelComicRealm
import com.globant.data.database.entity.MarvelThumbnailRealm
import com.globant.domain.entities.ComicList
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.entities.MarvelComicSummary
import com.globant.domain.entities.MarvelThumbnail
import io.realm.RealmList

class CharacterMapperLocal : BaseMapperRepository<MarvelCharacterRealm, MarvelCharacter> {

    override fun transform(type: MarvelCharacterRealm): MarvelCharacter = MarvelCharacter(
            type.id,
            type.name,
            type.description,
            transformThumbnail(type.thumbnail),
            transformComicList(type.comics)
    )

    override fun transformToRepository(type: MarvelCharacter): MarvelCharacterRealm = MarvelCharacterRealm(
            type.id,
            type.name,
            type.description,
            transformToThumbnailRepository(type.thumbnail),
            transformComicListRepository(type.comics)
    )

    private fun transformThumbnail(type: MarvelThumbnailRealm?): MarvelThumbnail = MarvelThumbnail(
            type?.path,
            type?.extension
    )

    private fun transformToThumbnailRepository(type: MarvelThumbnail?): MarvelThumbnailRealm = MarvelThumbnailRealm(
            type?.path,
            type?.extension
    )

    private fun transformComic(type: MarvelComicRealm?): MarvelComicSummary = MarvelComicSummary(
            type?.id,
            type?.title,
            type?.description
    )

    private fun transformComicRepository(type: MarvelComicSummary): MarvelComicRealm = MarvelComicRealm(
            type.id,
            type.title,
            type.description
    )

    private fun transformComicListRepository(type: ComicList?): ComicListRealm {
        val realmList = RealmList<MarvelComicRealm>()
        type?.items?.forEach {
            realmList.add(transformComicRepository(it))
        }
        return ComicListRealm(type?.available, type?.collectionURI, realmList)
    }

    private fun transformComicList(type: ComicListRealm?): ComicList {
        val comicList = mutableListOf<MarvelComicSummary>()
        type?.items?.forEach{
            comicList.add(transformComic(it))
        }
        return ComicList(type?.available, type?.collectionURI, comicList)
    }
}
