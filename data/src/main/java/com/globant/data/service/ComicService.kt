package com.globant.data.service

import com.globant.data.MarvelRequestGenerator
import com.globant.data.mapper.ComicMapperRemote
import com.globant.data.service.api.MarvelApi
import com.globant.domain.entities.Comic
import com.globant.domain.utils.Result

class ComicService {
    private val api: MarvelRequestGenerator = MarvelRequestGenerator()
    private val mapper: ComicMapperRemote = ComicMapperRemote()

    fun getComics(id: Int) : Result<List<Comic>> {
        val callResponse = api.createService(MarvelApi::class.java).getComics(id)
        callResponse.execute().let {
            if(it.isSuccessful) {
                it.body()?.data?.comics?.map { comicResponse -> mapper.transform(comicResponse) }?.let { response -> return Result.Success(response)}
            }
        }
        return Result.Failure(Exception(callResponse.execute().message()))
    }
}