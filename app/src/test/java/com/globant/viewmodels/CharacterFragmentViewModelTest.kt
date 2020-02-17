package com.globant.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetComicsUseCase
import com.globant.domain.utils.Result
import com.globant.myapplication.INVALID_ID
import com.globant.myapplication.UI_THREAD
import com.globant.myapplication.VALID_ID
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

class CharacterFragmentViewModelTest : AutoCloseKoinTest() {

    @ObsoleteCoroutinesApi
    var mainThreadSurrogate = newSingleThreadContext(UI_THREAD)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: CharacterFragmentViewModel

    private val marvelCharacterSuccess: Result.Success<MarvelCharacter> = mock()
    private val marvelCharacterFailure: Result.Failure = mock()
    private val marvelCharacter: MarvelCharacter = mock()
    private val comicSuccess: Result.Success<List<Comic>> = mock()
    private val comicFailure: Result.Failure = mock()
    private val comics: List<Comic> = mock()
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase = mock()
    private val getComicsUseCase: GetComicsUseCase = mock()

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = CharacterFragmentViewModel(getCharacterByIdUseCase, getComicsUseCase)
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun getCharacterByIdSuccess() {
        val liveDataUnderTest = viewModel.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(VALID_ID, true)).thenReturn(marvelCharacterSuccess)
        whenever(marvelCharacterSuccess.data).thenReturn(marvelCharacter)
        runBlocking {
            viewModel.getCharacterById(VALID_ID).join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
            .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
            .isEqualTo(Data(Status.SUCCESSFUL, data = marvelCharacter))
    }


    @Test
    fun getCharacterByIdFailure() {
        val liveDataUnderTest = viewModel.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(INVALID_ID, true)).thenReturn(marvelCharacterFailure)
        runBlocking {
            viewModel.getCharacterById(INVALID_ID).join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
            .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
            .isEqualTo(Data(Status.ERROR, data = null))

    }

    @Test
    fun getAllComicsSuccess() {
        val liveDataUnderTest = viewModel.comicState.testObserver()
        whenever(getComicsUseCase.invoke(VALID_ID)).thenReturn(comicSuccess)
        whenever(comicSuccess.data).thenReturn(comics)
        runBlocking {
            viewModel.getAllComics(VALID_ID).join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues[0]?.peekContent())
            .isEqualTo(Data(Status.LOADING, data = null))
        Truth.assertThat(liveDataUnderTest.observedValues[1]?.peekContent())
            .isEqualTo(Data(Status.SUCCESSFUL, data = comics))
    }

    @Test
    fun getAllComicsFailure() {
        val liveDataUnderTest = viewModel.comicState.testObserver()
        whenever(getComicsUseCase.invoke(INVALID_ID)).thenReturn(comicFailure)
        runBlocking {
            viewModel.getAllComics(INVALID_ID).join()
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

    companion object {

    }
}