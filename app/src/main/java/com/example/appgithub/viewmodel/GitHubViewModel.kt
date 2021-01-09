package com.example.appgithub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appgithub.data.repository.ItemRepository
import com.example.appgithub.data.repository.RepositoryViewContract
import com.example.appgithub.model.GitHubResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */
class GitHubViewModel (private val repository: RepositoryViewContract) : ViewModel() {
    private val movieList: MutableLiveData<GitHubResponse> = MutableLiveData()

    val movieListResult: LiveData<GitHubResponse> = movieList

    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingResult: LiveData<Boolean> = loading

    fun getAllMovies() {
        repository.getRepositories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe({
                setItemList(it)
            }, { throwable ->
                Throwable(throwable)
                logError(throwable.message)
            })

    }

    fun setItemList(it: GitHubResponse?) {
        movieList.value = it
    }

    fun setLoading(value: Boolean) {
        loading.value = value
    }

    fun logError(message: String?) {
        Log.i("LOG", "erro $message")
    }

    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GitHubViewModel(ItemRepository()) as T
        }
    }
}