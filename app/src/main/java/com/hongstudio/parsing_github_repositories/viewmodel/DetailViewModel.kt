package com.hongstudio.parsing_github_repositories.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.util.Event

class DetailViewModel : ViewModel() {
    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>> get() = _error

    private val _openRepository = MutableLiveData<Event<String>>()
    val openRepository: LiveData<Event<String>> get() = _openRepository

    private val _noDataImageVisible = MutableLiveData(false)
    val noDataImageVisible: LiveData<Boolean> get() = _noDataImageVisible

    private val _repo = MutableLiveData<RepositoryItemModel>()
    val repo: LiveData<RepositoryItemModel> = _repo

    fun init(repo: RepositoryItemModel?) {
        if (repo == null) {
            _noDataImageVisible.value = true
            _error.value = Event(R.string.failed_load_data)
            return
        }

        _repo.value = repo
    }

    fun onRepositoryLinkClick() {
        val url = _repo.value?.repositoryUrl ?: ""
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            _error.value = Event(R.string.wrong_web_url)
            return
        }
        _openRepository.value = Event(url)
    }
}