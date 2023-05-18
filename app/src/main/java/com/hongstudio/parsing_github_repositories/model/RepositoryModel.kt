package com.hongstudio.parsing_github_repositories.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryListModel(
    @SerializedName("items")
    val items: List<RepositoryItemModel>?
): Parcelable

@Parcelize
data class RepositoryItemModel(
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
    @SerializedName("html_url")
    val repositoryUrl: String,
): Parcelable

@Parcelize
data class OwnerModel(
    @SerializedName("login")
    val name: String,
    @SerializedName("avatar_url")
    val imageUrl: String,
): Parcelable