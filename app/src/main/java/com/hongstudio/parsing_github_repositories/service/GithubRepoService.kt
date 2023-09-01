package com.hongstudio.parsing_github_repositories.service

import com.hongstudio.parsing_github_repositories.model.RepoListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepoService {
    @GET("search/repositories")
    suspend fun getSearchedRepoList(@Query("q") keyword: String): RepoListModel
}
