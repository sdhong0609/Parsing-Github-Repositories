package com.hongstudio.parsing_github_repositories.util.extension

import com.hongstudio.parsing_github_repositories.data.local.OwnerModel
import com.hongstudio.parsing_github_repositories.data.local.RepoListModel
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiOwnerModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiRepoListModel
import com.hongstudio.parsing_github_repositories.data.remote.ApiRepoModel

fun ApiRepoListModel.toModel(): RepoListModel {
    return RepoListModel(
        totalCount = totalCount,
        items = items.map { it.toModel() }
    )
}

fun ApiRepoModel.toModel(): RepoModel {
    return RepoModel(
        name = name,
        owner = owner.toModel(),
        description = description,
        starsCount = starsCount,
        watchersCount = watchersCount,
        forksCount = forksCount,
        repoUrl = repoUrl,
    )
}

fun ApiOwnerModel.toModel(): OwnerModel {
    return OwnerModel(
        name = name,
        avatarUrl = avatarUrl,
    )
}
