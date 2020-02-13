package com.globant.data.service

import com.globant.data.MarvelRequestGenerator
import com.globant.data.ZERO
import com.globant.data.mapper.CharacterMapperRemote
import com.globant.data.service.api.MarvelApi
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.utils.Result

class CharacterService {

    private val api: MarvelRequestGenerator = MarvelRequestGenerator()
    private val mapper: CharacterMapperRemote = CharacterMapperRemote()

    fun getCharacterById(id: Int): Result<MarvelCharacter> {
        val callResponse = api.createService(MarvelApi::class.java).getCharacterById(id)
        val response = callResponse.execute()
        if (response != null) {
            if (response.isSuccessful) {
                response.body()?.data?.characters?.get(ZERO)?.let { mapper.transform(it) }?.let { return Result.Success(it) }
            }
            return Result.Failure(Exception(response.message()))
        }
        return Result.Failure(Exception("Bad request/response"))
    }

    fun getCharacters() : Result<List<MarvelCharacter>> {
        val callResponse = api.createService(MarvelApi::class.java).getCharacters()
        callResponse.execute().let {
            if (it.isSuccessful) {
                it.body()?.data?.characters?.map { characterResponse -> mapper.transform(characterResponse) }?.let { response -> return Result.Success(response) }
            }
            return Result.Failure(Exception(callResponse.execute().message()))
        }
    }
}
