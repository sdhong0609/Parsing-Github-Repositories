package com.hongstudio.parsing_github_repositories.service

import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepositoryService {
    @GET("search/repositories")
    fun getSearchedRepositoryList(@Query("q") keyword: String): Call<RepositoryListModel>
}

// presentation layer - view(Activity, Fragment, View), ViewModel
// domain layer(optional)

// data layer - Repository, Rest API, Local DB, SharedPreference, File, Firebase, etc.

/*
class ViewModel(
    val repository: GithubRepository
    // ....
) {
    fun getGithubRepoList() {
        return repository.getGithubRepoList()
    }
}
 */
