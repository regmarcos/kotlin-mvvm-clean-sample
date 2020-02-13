package com.globant.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.globant.domain.entities.MarvelCharacter
import com.globant.domain.usecases.GetCharacterByIdUseCase
import com.globant.domain.utils.Result
import com.globant.useCasesModule
import com.globant.utils.Data
import com.globant.utils.Status
import com.globant.viewmodels.CharacterViewModel
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
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

private const val VALID_ID = 1017100
private const val INVALID_ID = -1

class CharacterViewModelTest : AutoCloseKoinTest() {

    @ObsoleteCoroutinesApi
    private var mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: CharacterViewModel
    @Mock
    lateinit var marvelCharacterValidResult: Result.Success<MarvelCharacter>
    @Mock
    lateinit var marvelCharacterInvalidResult: Result.Failure
    @Mock
    lateinit var marvelCharacter: MarvelCharacter
    @Mock
    lateinit var exception: Exception

    private val getCharacterByIdUseCase: GetCharacterByIdUseCase by inject()

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        startKoin {
            modules(listOf(useCasesModule))
        }

        declareMock<GetCharacterByIdUseCase>()
        MockitoAnnotations.initMocks(this)
        subject = CharacterViewModel(getCharacterByIdUseCase)
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @After
    fun after() {
        stopKoin()
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
