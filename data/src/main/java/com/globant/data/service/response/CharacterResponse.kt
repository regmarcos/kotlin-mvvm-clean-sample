package com.globant.data.service.response

class CharacterResponse(
        val id: Int,
        val name: String,
        val description: String,
        val thumbnail: ThumbnailResponse,
        val comics: ComicListResponse
)
