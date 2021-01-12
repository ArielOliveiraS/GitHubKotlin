package com.example.appgithub.data.repository

import com.example.appgithub.data.remote.RetrofitService.Companion.service
import com.example.appgithub.model.GitHubResponse
import io.reactivex.Single

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */

class ItemRepository : RepositoryViewContract {
    override fun getRepositories(page: Int): Single<GitHubResponse> = service.getAllRepositories(page)
}