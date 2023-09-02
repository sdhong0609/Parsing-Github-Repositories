package com.hongstudio.parsing_github_repositories.ui.viewmodels

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.api.GithubRepoService
import com.hongstudio.parsing_github_repositories.data.remote.RepoListModel
import com.hongstudio.parsing_github_repositories.data.remote.RepoModel
import com.hongstudio.parsing_github_repositories.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoService: GithubRepoService
) : ViewModel() {

    private val _wifiImageVisible = MutableLiveData(false)
    val wifiImageVisible: LiveData<Boolean> get() = _wifiImageVisible

    private val _progressBarVisible = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean> get() = _progressBarVisible

    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>> get() = _error

    private val _repoList = MutableLiveData<List<RepoModel>>()
    val repoList: LiveData<List<RepoModel>> get() = _repoList

    private val _hideKeyboardEvent = MutableLiveData<Event<Unit>>()
    val hideKeyboardEvent: LiveData<Event<Unit>> get() = _hideKeyboardEvent

    private val _repoItemClickEvent = MutableLiveData<Event<RepoModel>>()
    val repoItemClickEvent: LiveData<Event<RepoModel>> get() = _repoItemClickEvent

    private val _keyword = MutableLiveData("")
    val keyword: LiveData<String> get() = _keyword

    private var searchedWord = ""
    private var page = INITIAL_PAGE

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
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

    fun afterKeywordTextChanged(editable: Editable?) {
        _keyword.value = editable.toString()
    }

    fun onRepoItemClick(item: RepoModel) {
        _repoItemClickEvent.value = Event(item)
    }

    fun searchRepositoriesAction() {
        searchedWord = _keyword.value?.trim() ?: ""
        if (searchedWord.isEmpty()) {
            _error.value = Event(R.string.please_input_keyword)
            return
        }

        _hideKeyboardEvent.value = Event(Unit)
        _wifiImageVisible.value = false
        _progressBarVisible.value = true
        _repoList.value = emptyList()
        page = INITIAL_PAGE

        loadRepoListData()
    }

    private fun loadRepoListData() {
        viewModelScope.launch(exceptionHandler) {
            val list = repoService.getSearchedRepoList(searchedWord, page)
            onLoadRepoListSuccess(list)
        }
    }

    private fun onLoadRepoListSuccess(list: RepoListModel) {
        _progressBarVisible.value = false

        if (list.items.isEmpty()) {
            _error.value = Event(R.string.there_is_no_result)
            return
        }

        _repoList.value = list.items
    }

    fun loadMoreRepoListData() {
        viewModelScope.launch(exceptionHandler) {
            val list = repoService.getSearchedRepoList(searchedWord, ++page)
            _repoList.value = _repoList.value?.plus(list.items)
        }
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}
