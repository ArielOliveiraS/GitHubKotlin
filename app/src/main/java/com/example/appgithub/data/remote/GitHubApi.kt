package com.example.appgithub.data.remote

import com.example.appgithub.model.GitHubResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */

interface GitHubApi {

    @GET("search/repositories?q=language:kotlin&sort=stars&page=1")
    fun getAllRepositories(): Single<GitHubResponse>
}