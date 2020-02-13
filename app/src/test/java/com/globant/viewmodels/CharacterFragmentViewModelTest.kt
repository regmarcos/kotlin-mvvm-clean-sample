package com.globant.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetComicsUseCase
import com.globant.domain.utils.Result
import com.globant.utils.Data
import com.globant.utils.Status
import com.google.common.truth.Truth
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CharacterFragmentViewModelTest : AutoCloseKoinTest() {

    companion object {
        const val VALID_ID = 1017100
        const val INVALID_ID = -1
    }

    @ObsoleteCoroutinesApi
    var mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: CharacterFragmentViewModel
    @Mock
    lateinit var marvelCharacterSuccess: Result.Success<MarvelCharacter>
    @Mock
    lateinit var marvelCharacterFailure: Result.Failure
    @Mock
    lateinit var marvelCharacter: MarvelCharacter
    @Mock
    lateinit var comicSuccess: Result.Success<List<Comic>>
    @Mock
    lateinit var comicFailure: Result.Failure
    @Mock
    lateinit var comics: List<Comic>
    @Mock
    lateinit var exception: Exception
    @Mock
    lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase
    @Mock
    lateinit var getComicsUseCase: GetComicsUseCase

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
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
        Mockito.`when`(getCharacterByIdUseCase.invoke(VALID_ID, true)).thenReturn(marvelCharacterSuccess)
        Mockito.`when`(marvelCharacterSuccess.data).thenReturn(marvelCharacter)
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
        Mockito.`when`(getCharacterByIdUseCase.invoke(INVALID_ID, true)).thenReturn(marvelCharacterFailure)
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
        Mockito.`when`(getComicsUseCase.invoke(VALID_ID)).thenReturn(comicSuccess)
        Mockito.`when`(comicSuccess.data).thenReturn(comics)
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
        Mockito.`when`(getComicsUseCase.invoke(INVALID_ID)).thenReturn(comicFailure)
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
}