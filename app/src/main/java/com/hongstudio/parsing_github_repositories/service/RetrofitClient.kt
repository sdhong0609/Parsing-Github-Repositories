package com.hongstudio.parsing_github_repositories.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubRepositoryService: GithubRepositoryService =
        retrofit.create(GithubRepositoryService::class.java)
}