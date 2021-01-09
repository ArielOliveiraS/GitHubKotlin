package com.example.appgithub.data.repository

import com.example.appgithub.model.GitHubResponse
import io.reactivex.Single

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */

interface RepositoryViewContract {
    fun getRepositories(): Single<GitHubResponse>
}