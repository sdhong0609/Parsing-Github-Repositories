package com.hongstudio.parsing_github_repositories.service

import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepositoryService {
    @GET("search/repositories")
    suspend fun getSearchedRepositoryList(@Query("q") keyword: String): RepositoryListModel
}
