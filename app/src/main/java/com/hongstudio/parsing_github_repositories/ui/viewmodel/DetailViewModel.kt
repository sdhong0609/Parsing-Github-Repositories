package com.hongstudio.parsing_github_repositories.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.data.remote.RepoModel
import com.hongstudio.parsing_github_repositories.util.Event

sealed interface DetailEvent {
    data class Error(val messageRes: Int) : DetailEvent
    data class OpenRepoUrl(val url: String) : DetailEvent
}

class DetailViewModel : ViewModel() {

    private val _noDataImageVisible = MutableLiveData(false)
    val noDataImageVisible: LiveData<Boolean> = _noDataImageVisible

    private val _repo = MutableLiveData<RepoModel>()
    val repo: LiveData<RepoModel> = _repo

    private val _detailEvent = MutableLiveData<Event<DetailEvent>>()
    val detailEvent: LiveData<Event<DetailEvent>> = _detailEvent

    fun init(repo: RepoModel?) {
        if (repo == null) {
            _noDataImageVisible.value = true
            _detailEvent.value = Event(DetailEvent.Error(R.string.failed_load_data))
            return
        }

        _repo.value = repo
    }

    fun onRepositoryLinkClick() {
        val url = _repo.value?.repoUrl ?: ""
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            _detailEvent.value = Event(DetailEvent.Error(R.string.wrong_web_url))
            return
        }
        _detailEvent.value = Event(DetailEvent.OpenRepoUrl(url))
    }
}
