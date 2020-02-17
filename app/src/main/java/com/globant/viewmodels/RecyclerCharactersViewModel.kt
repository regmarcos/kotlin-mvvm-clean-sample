package com.globant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.activities.CharactersActivity
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharactersUseCase
import com.globant.domain.usecases.GetRepositoryUseCase
import com.globant.domain.utils.Result
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.Status
import kotlinx.android.synthetic.main.activity_main_recyclerview.recycler_view_characters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerCharactersViewModel(val getCharactersUseCase: GetCharactersUseCase, val getCharactersDBUseCase: GetRepositoryUseCase) : ViewModel() {
    private var mutableMainState: MutableLiveData<Event<Data<List<MarvelCharacter>>>> = MutableLiveData()
    val mainState: LiveData<Event<Data<List<MarvelCharacter>>>>
        get() {
            return mutableMainState
        }

    fun requestAllCharacters() = viewModelScope.launch {
        mutableMainState.value = Event(Data(responseType = Status.LOADING))
        when (val result = withContext(Dispatchers.IO) { getCharactersUseCase()}){
            is Result.Failure -> {
                mutableMainState.value = Event(Data(responseType = Status.ERROR, error = result.exception))
            }
            is Result.Success -> {
                mutableMainState.value = Event(Data(responseType = Status.SUCCESSFUL, data = result.data))
            }
        }
    }

    fun requestAllCharactersFromDB() = viewModelScope.launch {
        mutableMainState.value = Event(Data(responseType = Status.LOADING))
        when (val result = withContext(Dispatchers.IO){ getCharactersDBUseCase()}) {
            is Result.Failure -> {
                mutableMainState.value = Event(Data(responseType = Status.ERROR, error = result.exception))
            }
            is Result.Success -> {
                mutableMainState.value = Event(Data(responseType = Status.SUCCESSFUL, data = result.data))
            }
        }
    }

    fun onRefreshFABClicked(activity: CharactersActivity) {
        activity.showCharacters(emptyList())
        requestAllCharacters()
    }

    fun onDeleteFABClicked(activity: CharactersActivity) {
        activity.showCharacters(emptyList())
    }

    fun onStorageFABClicked(activity: CharactersActivity) {
        activity.showCharacters(emptyList())
        requestAllCharactersFromDB()
    }
}