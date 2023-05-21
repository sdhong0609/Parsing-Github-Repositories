package com.hongstudio.parsing_github_repositories.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RepositoryListModel(
    @SerialName("items")
    val items: List<RepositoryItemModel>
) : Parcelable

@Parcelize
@Serializable
data class RepositoryItemModel(
    @SerialName("name")
    val repositoryName: String,
    @SerialName("owner")
    val owner: OwnerModel,
    @SerialName("description")
    val repositoryDescription: String = "",
    @SerialName("stargazers_count")
    val starsCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("html_url")
    val repositoryUrl: String,
) : Parcelable

@Parcelize
@Serializable
data class OwnerModel(
    @SerialName("login")
    val name: String,
    @SerialName("avatar_url")
    val imageUrl: String,
) : Parcelable
