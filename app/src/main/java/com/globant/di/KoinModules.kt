package com.globant.di

import com.globant.viewmodels.CharacterViewModel
import com.globant.viewmodels.RecyclerCharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { CharacterViewModel(get()) }
    viewModel { RecyclerCharactersViewModel(get()) }
}
