package com.hongstudio.parsing_github_repositories.util.extension

import com.hongstudio.parsing_github_repositories.data.local.OwnerModel
import com.hongstudio.parsing_github_repositories.data.local.RepoListModel
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiOwnerModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiRepoListModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiRepoModel

fun ApiRepoListModel.toLocalModel(): RepoListModel {
    return RepoListModel(
        items = items.map { it.toLocalModel() }
    )
}

fun ApiRepoModel.toLocalModel(): RepoModel {
    return RepoModel(
        name = name,
        owner = owner.toLocalModel(),
        description = description,
        starsCount = starsCount,
        watchersCount = watchersCount,
        forksCount = forksCount,
        repoUrl = repoUrl,
    )
}

fun ApiOwnerModel.toLocalModel(): OwnerModel {
    return OwnerModel(
        name = name,
        avatarUrl = avatarUrl,
    )
}
