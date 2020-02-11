package com.globant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetComicsUseCase
import com.globant.domain.utils.Result
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterFragmentViewModel(private val getCharacterById: GetCharacterByIdUseCase, private val getComicsUseCase: GetComicsUseCase): ViewModel() {
    private var mutableMainState: MutableLiveData<Event<Data<MarvelCharacter>>> = MutableLiveData()
    private var mutableComicState: MutableLiveData<Event<Data<List<Comic>>>> = MutableLiveData()
    val mainState: LiveData<Event<Data<MarvelCharacter>>>
        get() {
            return mutableMainState
        }
    val comicState: LiveData<Event<Data<List<Comic>>>>
        get(){
            return mutableComicState
        }

    fun getCharacterById(id: Int) = viewModelScope.launch {
        mutableMainState.value = Event(Data(responseType = Status.LOADING))
        when (val result = withContext(Dispatchers.IO) { getCharacterById(id, true) }) {
            is Result.Failure -> {
                mutableMainState.value = Event(Data(responseType = Status.ERROR, error = result.exception))
            }
            is Result.Success -> {
                mutableMainState.value = Event(Data(responseType = Status.SUCCESSFUL, data = result.data))
            }
        }
    }
    fun getAllComics(id: Int) = viewModelScope.launch {
        mutableComicState.value = Event(Data(responseType = Status.LOADING))
        when (val result = withContext(Dispatchers.IO) { getComicsUseCase(id) }) {
            is Result.Failure -> {
                mutableComicState.value = Event(Data(responseType = Status.ERROR, error = result.exception))
            }
            is Result.Success -> {
                mutableComicState.value = Event(Data(responseType = Status.SUCCESSFUL, data = result.data))
            }
        }
    }
}