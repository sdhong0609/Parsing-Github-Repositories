package com.hongstudio.parsing_github_repositories.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

object RetrofitClient {
    private const val baseUrl = "https://api.github.com/"
    private const val contentType = "application/json"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(MediaType.get(contentType)))
        .build()

    val githubRepositoryService: GithubRepositoryService = retrofit.create(GithubRepositoryService::class.java)
}
