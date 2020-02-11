package com.globant.data.service.response

class ComicListResponse(
        val available: Int?,
        val collectionURI: String?,
        val items: MutableList<MarvelComicResponse>
)