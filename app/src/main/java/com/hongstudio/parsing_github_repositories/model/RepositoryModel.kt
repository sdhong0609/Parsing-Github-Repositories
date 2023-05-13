package com.hongstudio.parsing_github_repositories.model

import com.google.gson.annotations.SerializedName

data class RepositoryModel(
    @SerializedName("items")
    val items: List<ItemModel>?
)

data class ItemModel(
    @SerializedName("name")
    val repositoryName: String,
    @SerializedName("owner")
    val owner: OwnerModel,
    @SerializedName("description")
    val repositoryDescription: String,
    @SerializedName("stargazers_count")
    val starsCount: Int,
    @SerializedName("watchers_count")
    val watchersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,

    // DetailActivity에 추가되는 프로퍼티
    @SerializedName("html_url")
    val repositoryUrl: String,
)

data class OwnerModel(
    @SerializedName("login")
    val ownerName: String,
    @SerializedName("avatar_url")
    val ownerImageUrl: String,
)