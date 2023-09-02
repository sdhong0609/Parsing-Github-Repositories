package com.hongstudio.parsing_github_repositories.api

import com.hongstudio.parsing_github_repositories.data.remote.RepoListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepoService {
    @GET("search/repositories")
    suspend fun getSearchedRepoList(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): RepoListModel
}
