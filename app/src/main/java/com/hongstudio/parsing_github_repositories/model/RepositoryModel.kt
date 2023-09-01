package com.hongstudio.parsing_github_repositories.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RepoListModel(
    @SerialName("items")
    val items: List<RepoModel>
) : Parcelable

@Parcelize
@Serializable
data class RepoModel(
    @SerialName("name")
    val name: String,
    @SerialName("owner")
    val owner: OwnerModel,
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
) : Parcelable

@Parcelize
@Serializable
data class OwnerModel(
    @SerialName("login")
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
) : Parcelable
