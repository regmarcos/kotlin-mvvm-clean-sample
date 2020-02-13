package com.globant.data.database.entity

import com.globant.data.EMPTY_STRING
import com.globant.data.DEFAULT_ID
import com.globant.data.ZERO
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MarvelCharacterRealm(
        @PrimaryKey
        var id: Int = DEFAULT_ID,
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        var thumbnail: MarvelThumbnailRealm? = MarvelThumbnailRealm(EMPTY_STRING, EMPTY_STRING),
        var comics: ComicListRealm? = ComicListRealm(ZERO, EMPTY_STRING, null)
) : RealmObject()
