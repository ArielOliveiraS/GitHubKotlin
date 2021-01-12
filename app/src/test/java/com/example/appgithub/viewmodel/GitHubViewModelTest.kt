package com.example.appgithub.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appgithub.data.repository.RepositoryViewContract
import com.example.appgithub.model.GitHubResponse
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by arieloliveira on 11/01/21 for AppGitHub.
 */
class GitHubViewModelTest {

    @Rule
    @JvmField
    val rule : TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repositoryViewcontract: RepositoryViewContract

    @Mock
    lateinit var repositories: GitHubResponse

    lateinit var viewModel: GitHubViewModel

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({ Schedulers.trampoline()})
        MockitoAnnotations.initMocks(this)
        viewModel = instantiateViewModel()
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repositoryViewcontract, repositories)
    }


    @Test
    fun test_getAllMovies_success() {
        //Given
        val viewModelSpy = Mockito.spy(viewModel)
        val listItems = Single.just(GitHubResponse(1, false, arrayListOf()))
        var items = GitHubResponse(1, false, arrayListOf())
        listItems.subscribe { result -> items = result }

        Mockito.`when`(repositoryViewcontract.getRepositories()).thenReturn(listItems)
        Mockito.doNothing().`when`(viewModelSpy).setItemList(items)
        Mockito.doNothing().`when`(viewModelSpy).setLoading(ArgumentMatchers.anyBoolean())

        //Act
        viewModelSpy.getAllRepositories()

        //Assert
        Mockito.verify(viewModelSpy, Mockito.times(1)).getAllRepositories()
        Mockito.verify(viewModelSpy, Mockito.never()).logError(ArgumentMatchers.anyString())
        Mockito.verify(repositoryViewcontract, Mockito.times(1)).getRepositories()
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(true)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(false)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setItemList(items)
    }

    @Test
    fun test_getAllMovies_error() {
        //Given
        val errorMessage = "Error Message"
        val throwable = Throwable(errorMessage)
        val viewModelSpy = Mockito.spy(viewModel)

        Mockito.`when`(repositoryViewcontract.getRepositories()).thenReturn(Single.error(throwable))
        Mockito.doNothing().`when`(viewModelSpy).setLoading(ArgumentMatchers.anyBoolean())
        Mockito.doNothing().`when`(viewModelSpy).logError(errorMessage)

        //Act
        viewModelSpy.getAllRepositories()

        //Assert
        Mockito.verify(viewModelSpy, Mockito.times(1)).getAllRepositories()
        Mockito.verify(viewModelSpy, Mockito.never()).setItemList(ArgumentMatchers.any())
        Mockito.verify(repositoryViewcontract, Mockito.times(1)).getRepositories()
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(true)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(false)
        Mockito.verify(viewModelSpy, Mockito.times(1)).logError(errorMessage)
    }

    @Test
    fun test_setItemList() {
        viewModel.setItemList(repositories)

        Assert.assertEquals(repositories, viewModel.repositorytResult.value)
    }

    @Test
    fun test_setLoading() {
        viewModel.setLoading(true)

        Assert.assertTrue(viewModel.loadingResult.value ?: false)
    }


    private fun instantiateViewModel(): GitHubViewModel {
        val viewModel = GitHubViewModel(repositoryViewcontract)
        return viewModel
    }
}