package com.globant.data.database.entity

import com.globant.data.EMPTY_STRING
import io.realm.RealmObject

open class MarvelThumbnailRealm(
        var path: String? = EMPTY_STRING,
        var extension: String? = EMPTY_STRING
): RealmObject()