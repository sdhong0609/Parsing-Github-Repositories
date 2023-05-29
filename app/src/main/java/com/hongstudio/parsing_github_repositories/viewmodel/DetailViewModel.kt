package com.hongstudio.parsing_github_repositories.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.util.Event

class DetailViewModel(val repo: RepositoryItemModel?) : ViewModel() {
    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>> get() = _error

    private val _openRepository = MutableLiveData<Event<Unit>>()
    val openRepository: LiveData<Event<Unit>> get() = _openRepository

    private val _noDataImageVisible = MutableLiveData(false)
    val noDataImageVisible: LiveData<Boolean> get() = _noDataImageVisible

    fun onRepositoryLinkClick() {
        if (!Patterns.WEB_URL.matcher(repo?.repositoryUrl ?: "").matches()) {
            _error.value = Event(R.string.wrong_web_url)
            return
        }
        _openRepository.value = Event(Unit)
    }

    fun repoNullCheck() {
        if (repo == null) {
            _noDataImageVisible.value = true
            _error.value = Event(R.string.failed_load_data)
        }
    }
}
