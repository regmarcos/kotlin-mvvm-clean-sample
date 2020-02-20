package com.globant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharactersUseCase
import com.globant.domain.usecases.GetRepositoryUseCase
import com.globant.domain.utils.Result
import com.globant.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerCharactersViewModel(val getCharactersUseCase: GetCharactersUseCase, val getCharactersDBUseCase: GetRepositoryUseCase) : ViewModel() {
    private var mutableMainState: MutableLiveData<Event<RecyclerData<List<MarvelCharacter>>>> = MutableLiveData()
    val mainState: LiveData<Event<RecyclerData<List<MarvelCharacter>>>>
        get() {
            return mutableMainState
        }

    fun requestAllCharacters() = viewModelScope.launch {
        mutableMainState.value = Event(RecyclerData(responseType = RecyclerStatus.LOADING))
        when (val result = withContext(Dispatchers.IO) { getCharactersUseCase() }) {
            is Result.Failure -> {
                mutableMainState.postValue(Event(RecyclerData(responseType = RecyclerStatus.ERROR, error = result.exception)))
            }
            is Result.Success -> {
                mutableMainState.postValue(Event(RecyclerData(responseType = RecyclerStatus.SUCCESSFUL, data = result.data)))
            }
        }
    }

    fun requestAllCharactersFromDB() = viewModelScope.launch {
        mutableMainState.value = Event(RecyclerData(responseType = RecyclerStatus.LOADING))
        when (val result = withContext(Dispatchers.IO) { getCharactersDBUseCase() }) {
            is Result.Failure -> {
                mutableMainState.postValue(Event(RecyclerData(responseType = RecyclerStatus.ERROR, error = result.exception)))
            }
            is Result.Success -> {
                mutableMainState.postValue(Event(RecyclerData(responseType = RecyclerStatus.SUCCESSFUL, data = result.data)))
            }
        }
    }

    fun clearScreen() {
        mutableMainState.value = Event(RecyclerData(responseType = RecyclerStatus.CLEAR))
    }
}

data class RecyclerData<RequestData>(var responseType: RecyclerStatus, var data: RequestData? = null, var error: Exception? = null)

enum class RecyclerStatus {
    SUCCESSFUL,
    ERROR,
    LOADING,
    CLEAR
}