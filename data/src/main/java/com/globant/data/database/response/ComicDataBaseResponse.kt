package com.globant.data.database.response

import com.globant.data.service.response.ComicResponse
import com.google.gson.annotations.SerializedName

class ComicDataBaseResponse<T>(
        @SerializedName("results") val comics: List<ComicResponse>
)