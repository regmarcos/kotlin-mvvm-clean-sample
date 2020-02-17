package com.globant.data.database

import com.globant.data.database.entity.MarvelCharacterRealm
import com.globant.data.mapper.CharacterMapperLocal
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.utils.Result
import io.realm.Realm

class CharacterDatabase {

    fun getCharacterById(id: Int): Result<MarvelCharacter> {
        val mapper = CharacterMapperLocal()
        Realm.getDefaultInstance().use {
            val character = it.where(MarvelCharacterRealm::class.java).equalTo("id", id).findFirst()
            character?.let { return Result.Success(mapper.transform(character)) }
            return Result.Failure(Exception("Character not found"))
        }
    }
    fun getAllCharacters(): Result<List<MarvelCharacter>> {
        val mapper = CharacterMapperLocal()
        var allCharacters = emptyList<MarvelCharacter>()
        Realm.getDefaultInstance().use {realm ->
            val result = realm.where(MarvelCharacterRealm::class.java).findAll()
            result.let {
                allCharacters = realm.copyFromRealm(it).map { result -> mapper.transform(result) }
                return Result.Success(allCharacters)
            }
        }
        return Result.Failure(Exception("Not found"))
    }

    fun insertOrUpdateCharacter(character: MarvelCharacter) {
        val mapperLocal = CharacterMapperLocal()
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                realm.insertOrUpdate(mapperLocal.transformToRepository(character))
            }
        }
    }
}
