package com.globant.data.database.entity

import com.globant.data.EMPTY_STRING
import com.globant.data.ZERO
import io.realm.RealmObject

open class ComicRealm(
        var id: Int = ZERO,
        var title: String = EMPTY_STRING,
        var thumbnail: MarvelThumbnailRealm? = MarvelThumbnailRealm(EMPTY_STRING, EMPTY_STRING)
): RealmObject()