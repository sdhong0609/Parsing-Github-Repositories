package com.hongstudio.parsing_github_repositories.di

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

    /*
     TODO: GitHub Personal Access Token을 직접 코드에 작성하면 GitHub가 commit 내용 중 token이 있다는 것을 감지하여
      해당 token을 revoke(폐지)하고 새로운 token을 발급해야 한다고 경고를 보낸다.
      따라서 token을 어떻게 하면 직접 작성하지 않고 Header에 추가할지 고민중.
     */
//    private val authInterceptor = Interceptor { chain ->
//        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", "Bearer $authToken")
//            .build()
//        chain.proceed(newRequest)
//    }

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
        .client(provideOkHttpClient(loggingInterceptor))
        .addConverterFactory(json.asConverterFactory(contentType.toMediaType()))
        .build()

    @Singleton
    @Provides
    fun provideGithubRepoService(retrofit: Retrofit): GithubRepoService =
        retrofit.create(GithubRepoService::class.java)
}
