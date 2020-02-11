package com.globant.data.database.entity

import com.globant.domain.utils.DEFAULT_ID
import com.globant.domain.utils.EMPTY_STRING
import io.realm.RealmObject

open class MarvelComicRealm (
        var id: Int? = DEFAULT_ID,
        var title: String? = EMPTY_STRING,
        var description: String? = EMPTY_STRING
): RealmObject()