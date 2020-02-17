package com.globant.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharactersUseCase
import com.globant.domain.utils.Result
import com.globant.myapplication.UI_THREAD
import com.globant.utils.Data
import com.globant.utils.Status
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest

class RecyclerCharactersViewModelTest : AutoCloseKoinTest() {

    @ObsoleteCoroutinesApi
    var mainThreadSurrogate = newSingleThreadContext(UI_THREAD)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: RecyclerCharactersViewModel

    private val marvelCharacterListSuccess: Result.Success<List<MarvelCharacter>> = mock()
    private val marvelCharacterListFailure: Result.Failure = mock()
    private val marvelCharacterList: List<MarvelCharacter> = mock()
    private val getCharactersUseCase: GetCharactersUseCase = mock()

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
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
        whenever(getCharactersUseCase.invoke()).thenReturn(marvelCharacterListSuccess)
        whenever(marvelCharacterListSuccess.data).thenReturn(marvelCharacterList)
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
        whenever(getCharactersUseCase.invoke()).thenReturn(marvelCharacterListFailure)
        runBlocking {
            viewModel.requestAllCharacters().join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
            .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
            .isEqualTo(Data(Status.ERROR, data = null))
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