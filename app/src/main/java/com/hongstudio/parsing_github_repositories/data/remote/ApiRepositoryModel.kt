package com.hongstudio.parsing_github_repositories.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiRepoListModel(
    @SerialName("total_count")
    val totalCount: Long,
    @SerialName("items")
    val items: List<ApiRepoModel>
)

@Serializable
data class ApiRepoModel(
    @SerialName("name")
    val name: String,
    @SerialName("owner")
    val owner: ApiOwnerModel,
    @SerialName("description")
    val description: String = "",
    @SerialName("stargazers_count")
    val starsCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("html_url")
    val repoUrl: String,
)

@Serializable
data class ApiOwnerModel(
    @SerialName("login")
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
)
