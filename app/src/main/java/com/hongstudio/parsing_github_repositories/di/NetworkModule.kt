package com.hongstudio.parsing_github_repositories.di

import com.hongstudio.parsing_github_repositories.BuildConfig
import com.hongstudio.parsing_github_repositories.api.GithubRepoService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val baseUrl = "https://api.github.com/"
    private const val contentType = "application/json"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.GITHUB_TOKEN}")
            .build()
        chain.proceed(newRequest)
    }

    private fun provideOkHttpClient(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder().run {
        interceptors.forEach {
            addInterceptor(it)
        }
        build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(provideOkHttpClient(loggingInterceptor, authInterceptor))
        .addConverterFactory(json.asConverterFactory(contentType.toMediaType()))
        .build()

    @Singleton
    @Provides
    fun provideGithubRepoService(retrofit: Retrofit): GithubRepoService =
        retrofit.create(GithubRepoService::class.java)
}
