package com.hongstudio.parsing_github_repositories.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoListModel(
    val items: List<RepoModel>
) : Parcelable

@Parcelize
data class RepoModel(
    val name: String,
    val owner: OwnerModel,
    val description: String = "",
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val repoUrl: String,
) : Parcelable

@Parcelize
data class OwnerModel(
    val name: String,
    val avatarUrl: String,
) : Parcelable
