package com.hongstudio.parsing_github_repositories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.model.RepoListModel
import com.hongstudio.parsing_github_repositories.model.RepoModel
import com.hongstudio.parsing_github_repositories.service.GithubRepositoryService
import com.hongstudio.parsing_github_repositories.util.Event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(private val repoService: GithubRepositoryService) : ViewModel() {
    private val _wifiImageVisible = MutableLiveData(false)
    val wifiImageVisible: LiveData<Boolean> get() = _wifiImageVisible

    private val _progressBarVisible = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean> get() = _progressBarVisible

    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>> get() = _error

    private val _repoList = MutableLiveData<List<RepoModel>>()
    val repoList: LiveData<List<RepoModel>> get() = _repoList

    private val _hideKeyboard = MutableLiveData<Event<Unit>>()
    val hideKeyboard: LiveData<Event<Unit>> get() = _hideKeyboard

    private val _repoItemClickEvent = MutableLiveData<Event<RepoModel>>()
    val repoItemClickEvent: LiveData<Event<RepoModel>> get() = _repoItemClickEvent

    val keyword = MutableLiveData("")

    fun onRepoItemClick(item: RepoModel) {
        _repoItemClickEvent.value = Event(item)
    }

    fun searchRepositoriesAction() {
        val searchedWord = keyword.value?.trim() ?: ""
        if (searchedWord.isEmpty()) {
            _error.value = Event(R.string.please_input_keyword)
            return
        }

        _hideKeyboard.value = Event(Unit)
        _wifiImageVisible.value = false
        _progressBarVisible.value = true
        _repoList.value = emptyList()

        loadRepositoriesData(searchedWord)
    }

    private fun loadRepositoriesData(keyword: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            _progressBarVisible.value = false
            when (t) {
                is IOException -> {
                    _wifiImageVisible.value = true
                    _error.value = Event(R.string.there_is_no_interent)
                }

                else -> {
                    _error.value = Event(R.string.something_wrong_happened)
                }
            }
        }

        viewModelScope.launch(exceptionHandler) {
            val list = repoService.getSearchedRepositoryList(keyword)
            onLoadRepositoriesSuccess(list)
        }
    }

    private fun onLoadRepositoriesSuccess(list: RepoListModel) {
        _progressBarVisible.value = false

        if (list.items.isEmpty()) {
            _error.value = Event(R.string.there_is_no_result)
            return
        }

        _repoList.value = list.items
    }
}
