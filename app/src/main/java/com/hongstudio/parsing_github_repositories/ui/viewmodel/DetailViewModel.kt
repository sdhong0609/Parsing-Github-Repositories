package com.hongstudio.parsing_github_repositories.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface DetailEvent {
    data class Error(val messageRes: Int) : DetailEvent
    data class OpenRepoUrl(val url: String) : DetailEvent
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noDataImageVisible = MutableLiveData(false)
    val noDataImageVisible: LiveData<Boolean> = _noDataImageVisible

    private val _detailEvent = MutableLiveData<Event<DetailEvent>>()
    val detailEvent: LiveData<Event<DetailEvent>> = _detailEvent

    private val _repo = savedStateHandle.getLiveData<RepoModel>(REPO_KEY)
    val repo: LiveData<RepoModel> = _repo

    fun onRepositoryLinkClick() {
        val url = _repo.value?.repoUrl ?: ""
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            _detailEvent.value = Event(DetailEvent.Error(R.string.wrong_web_url))
            return
        }
        _detailEvent.value = Event(DetailEvent.OpenRepoUrl(url))
    }

    companion object {
        private const val REPO_KEY = "REPO_KEY"
    }
}
