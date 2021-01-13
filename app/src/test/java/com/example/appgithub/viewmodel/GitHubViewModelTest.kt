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
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline()}
        MockitoAnnotations.initMocks(this)
        viewModel = instantiateViewModel()
    }

    @Test
    fun test_getAllRepos_success() {
        //Given
        val viewModelSpy = Mockito.spy(viewModel)
        val listItems = Single.just(GitHubResponse(1, false, arrayListOf()))
        var items = GitHubResponse(1, false, arrayListOf())
        listItems.subscribe { result -> items = result }

        Mockito.`when`(repositoryViewcontract.getRepositories(1)).thenReturn(listItems)
        Mockito.doNothing().`when`(viewModelSpy).setItemList(items)
        Mockito.doNothing().`when`(viewModelSpy).setLoading(ArgumentMatchers.anyBoolean())

        //Act
        viewModelSpy.getAllRepositories(1)

        //Assert
        Mockito.verify(viewModelSpy, Mockito.times(1)).getAllRepositories(1)
        Mockito.verify(repositoryViewcontract, Mockito.times(1)).getRepositories(1)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(true)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(false)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setItemList(items)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setError(false)
    }

    @Test
    fun test_getAllRepos_error() {
        //Given
        val errorMessage = "Error Message"
        val throwable = Throwable(errorMessage)
        val viewModelSpy = Mockito.spy(viewModel)

        Mockito.`when`(repositoryViewcontract.getRepositories(1)).thenReturn(Single.error(throwable))
        Mockito.doNothing().`when`(viewModelSpy).setLoading(ArgumentMatchers.anyBoolean())

        //Act
        viewModelSpy.getAllRepositories(1)

        //Assert
        Mockito.verify(viewModelSpy, Mockito.times(1)).getAllRepositories(1)
        Mockito.verify(viewModelSpy, Mockito.never()).setItemList(ArgumentMatchers.any())
        Mockito.verify(repositoryViewcontract, Mockito.times(1)).getRepositories(1)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(true)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setLoading(false)
        Mockito.verify(viewModelSpy, Mockito.times(1)).setError(true)
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
        return GitHubViewModel(repositoryViewcontract) //chamando o mock ao inicializar o view model
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repositoryViewcontract, repositories) //verifica interaçoes dos mocks
    }
}