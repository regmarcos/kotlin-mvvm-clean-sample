package com.globant.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.usecases.GetRepositoryUseCase
import com.globant.domain.utils.Result
import com.globant.utils.Data
import com.globant.utils.Status
import com.globant.viewmodels.CharacterViewModel
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
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
import org.koin.test.mock.declareMock

import org.mockito.Mockito.`when` as whenever

class CharacterViewModelTest : AutoCloseKoinTest() {


    @ObsoleteCoroutinesApi
    private var mainThreadSurrogate = newSingleThreadContext(UI_THREAD)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: CharacterViewModel
    private val marvelCharacterValidResult: Result.Success<MarvelCharacter> = mock()
    private val marvelCharacterInvalidResult: Result.Failure = mock()
    private val marvelCharacter: MarvelCharacter = mock()
    private val exception: Exception = mock()

    private val getCharacterByIdUseCase: GetCharacterByIdUseCase = mock()

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        //declareMock<GetCharacterByIdUseCase>()
        //declareMock<GetRepositoryUseCase>()
        subject = CharacterViewModel(getCharacterByIdUseCase)
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun onSearchRemoteTestSuccessful() {
        val liveDataUnderTest = subject.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(VALID_ID, true)).thenReturn(marvelCharacterValidResult)
        whenever(marvelCharacterValidResult.data).thenReturn(marvelCharacter)
        runBlocking {
            subject.onSearchRemoteClicked(VALID_ID).join()
        }
        Truth.assertThat(liveDataUnderTest.observedValues)
            .isEqualTo(listOf(Data(Status.LOADING), Data(Status.SUCCESSFUL, data = marvelCharacter)))

    }

    @Test
    fun onSearchRemoteTestError() {
        val liveDataUnderTest = subject.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(INVALID_ID, true)).thenReturn(marvelCharacterInvalidResult)
        whenever(marvelCharacterInvalidResult.exception).thenReturn(exception)

        runBlocking {
            subject.onSearchRemoteClicked(INVALID_ID).join()
        }

        Truth.assertThat(liveDataUnderTest.observedValues)
            .isEqualTo(listOf(Data(Status.LOADING), Data(Status.ERROR, data = null, error = exception)))
    }

    @Test
    fun onSearchLocalSuccessful() {
        val liveDataUnderTest = subject.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(VALID_ID, false)).thenReturn(marvelCharacterValidResult)
        whenever(marvelCharacterValidResult.data).thenReturn(marvelCharacter)

        runBlocking {
            subject.onSearchLocalClicked(VALID_ID).join()
        }

        Truth.assertThat(liveDataUnderTest.observedValues)
            .isEqualTo(listOf(Data(Status.LOADING), Data(Status.SUCCESSFUL, data = marvelCharacter)))
    }

    @Test
    fun onSearchLocalTestError() {
        val liveDataUnderTest = subject.mainState.testObserver()
        whenever(getCharacterByIdUseCase.invoke(INVALID_ID, true)).thenReturn(marvelCharacterInvalidResult)
        whenever(marvelCharacterInvalidResult.exception).thenReturn(exception)

        runBlocking {
            subject.onSearchRemoteClicked(INVALID_ID).join()
        }

        Truth.assertThat(liveDataUnderTest.observedValues)
            .isEqualTo(listOf(Data(Status.LOADING), Data(Status.ERROR, data = null, error = exception)))
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
