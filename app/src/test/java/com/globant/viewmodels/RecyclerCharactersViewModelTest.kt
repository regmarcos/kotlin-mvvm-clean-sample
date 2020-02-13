package com.globant.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetCharactersUseCase
import com.globant.domain.usecases.GetComicsUseCase
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import com.globant.domain.utils.Result
import com.globant.utils.Data
import com.globant.utils.Status
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RecyclerCharactersViewModelTest: AutoCloseKoinTest() {

    @ObsoleteCoroutinesApi
    var mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: RecyclerCharactersViewModel
    @Mock
    lateinit var marvelCharacterListSuccess: Result.Success<List<MarvelCharacter>>
    @Mock
    lateinit var marvelCharacterListFailure: Result.Failure
    @Mock
    lateinit var marvelCharacterList: List<MarvelCharacter>
    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        viewModel = RecyclerCharactersViewModel(getCharactersUseCase)
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun getListOfCharactersSuccess() {
        val liveDataUnderTest = viewModel.mainState.testObserver()
        Mockito.`when`(getCharactersUseCase.invoke()).thenReturn(marvelCharacterListSuccess)
        Mockito.`when`(marvelCharacterListSuccess.data).thenReturn(marvelCharacterList)
        runBlocking {
            viewModel.requestAllCharacters().join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
                .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
                .isEqualTo(Data(Status.SUCCESSFUL, data = marvelCharacterList))
    }

    @Test
    fun getListOfCharactersFailure() {
        val liveDataUnderTest = viewModel.mainState.testObserver()
        Mockito.`when`(getCharactersUseCase.invoke()).thenReturn(marvelCharacterListSuccess)
        Mockito.`when`(marvelCharacterListSuccess.data).thenReturn(marvelCharacterList)
        runBlocking {
            viewModel.requestAllCharacters().join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
                .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
                .isEqualTo(Data(Status.SUCCESSFUL, data = marvelCharacterList))
    }

    class TestObserver<T> : Observer<T> {
        val observedValues = mutableListOf<T?>()
        override fun onChanged(value: T?) {
            observedValues.add(value)
        }
    }

    private fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
        observeForever(it)
    }
}