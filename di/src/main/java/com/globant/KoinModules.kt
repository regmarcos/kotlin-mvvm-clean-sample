package com.globant

import com.globant.data.database.CharacterDatabase
import com.globant.data.database.ComicDatabase
import com.globant.data.repositories.ComicRepositoryImpl
import com.globant.data.repositories.MarvelCharacterRepositoryImpl
import com.globant.data.service.CharacterService
import com.globant.data.service.ComicService
import com.globant.domain.repositories.ComicRepository
import com.globant.domain.repositories.MarvelCharacterRepository
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetCharactersUseCase
import com.globant.domain.usecases.GetComicsUseCase
import org.koin.dsl.module

val repositoriesModule = module {
    single { CharacterService() }
    single { CharacterDatabase() }
    single<MarvelCharacterRepository> { MarvelCharacterRepositoryImpl(get(), get()) }
    single { ComicService() }
    single { ComicDatabase() }
    single<ComicRepository> { ComicRepositoryImpl(get(), get()) }
}
val useCasesModule = module {
    single { GetCharacterByIdUseCase() }
    single { GetCharactersUseCase() }
    single { GetComicsUseCase() }
}