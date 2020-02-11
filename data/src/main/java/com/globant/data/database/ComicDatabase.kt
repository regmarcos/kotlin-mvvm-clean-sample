package com.globant.data.database

import com.globant.data.database.entity.ComicRealm
import com.globant.data.mapper.ComicMapperLocal
import com.globant.domain.utils.Result
import com.globant.domain.entities.Comic
import io.realm.Realm

class ComicDatabase {

    fun getComicById(id: Int): Result<Comic> {
        val mapper = ComicMapperLocal()
        Realm.getDefaultInstance().use {
            val comic = it.where(ComicRealm::class.java).equalTo("id", id).findFirst()
            comic?.let { return Result.Success(mapper.transform(comic)) }
            return Result.Failure(Exception("Comic not found"))
        }
    }
    fun insertOrUpdateComic(comic: Comic) {
        val mapper = ComicMapperLocal()
        Realm.getDefaultInstance().use {
            it.executeTransaction{
                realm -> realm.insertOrUpdate(mapper.transformToRepository(comic))
            }
        }
    }
}