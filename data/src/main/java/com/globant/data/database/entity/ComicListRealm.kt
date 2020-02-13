package com.globant.data.database.entity

import com.globant.data.EMPTY_STRING
import com.globant.data.ZERO
import io.realm.RealmList
import io.realm.RealmObject

open class ComicListRealm(
        var available: Int? = ZERO,
        var collectionURI: String? = EMPTY_STRING,
        var items: RealmList<MarvelComicRealm>? = null
):RealmObject()