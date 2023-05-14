package com.hongstudio.parsing_github_repositories.retrofitinterface

import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkServiceInterface {
    @GET("search/repositories")
    fun getSearchedResult(@Query("q") searchedWord: String): Call<RepositoryListModel>
}