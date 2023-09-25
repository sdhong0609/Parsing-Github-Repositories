package com.hongstudio.parsing_github_repositories.ui.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.api.GithubRepoService
import com.hongstudio.parsing_github_repositories.data.local.RepoListModel
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.util.Event
import com.hongstudio.parsing_github_repositories.util.extension.toLocalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface HomeEvent {
    data class Error(val messageRes: Int) : HomeEvent
    data class RepoItemClick(val item: RepoModel) : HomeEvent
    object HideKeyboard : HomeEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoService: GithubRepoService
) : ViewModel() {

    private val _wifiImageVisible = MutableLiveData(false)
    val wifiImageVisible: LiveData<Boolean> = _wifiImageVisible

    private val _progressBarVisible = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean> = _progressBarVisible

    private val _repoList = MutableLiveData<List<RepoModel>>()
    val repoList: LiveData<List<RepoModel>> = _repoList

    private val _keyword = MutableLiveData("")
    val keyword: LiveData<String> = _keyword

    private val _homeEvent = MutableLiveData<Event<HomeEvent>>()
    val homeEvent: LiveData<Event<HomeEvent>> = _homeEvent

    private var searchedWord = ""
    private var page = INITIAL_PAGE

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _progressBarVisible.value = false
        when (t) {
            is IOException -> {
                _wifiImageVisible.value = true
                _homeEvent.value = Event(HomeEvent.Error(R.string.there_is_no_interent))
            }

            else -> {
                _wifiImageVisible.value = false
                _homeEvent.value = Event(HomeEvent.Error(R.string.something_wrong_happened))
            }
        }
    }

    fun afterKeywordTextChanged(editable: Editable?) {
        _keyword.value = editable.toString()
    }

    fun onRepoItemClick(item: RepoModel) {
        _homeEvent.value = Event(HomeEvent.RepoItemClick(item))
    }

    fun searchRepositoriesAction() {
        searchedWord = _keyword.value?.trim() ?: ""
        if (searchedWord.isEmpty()) {
            _homeEvent.value = Event(HomeEvent.Error(R.string.please_input_keyword))
            return
        }

        _homeEvent.value = Event(HomeEvent.HideKeyboard)
        _wifiImageVisible.value = false
        _progressBarVisible.value = true
        _repoList.value = emptyList()
        page = INITIAL_PAGE

        loadRepoListData()
    }

    private fun loadRepoListData() {
        viewModelScope.launch(exceptionHandler) {
            val list = repoService.getSearchedRepoList(searchedWord, page, PER_PAGE).toLocalModel()
            onLoadRepoListSuccess(list)
        }
    }

    private fun onLoadRepoListSuccess(list: RepoListModel) {
        _progressBarVisible.value = false

        if (list.items.isEmpty()) {
            _homeEvent.value = Event(HomeEvent.Error(R.string.there_is_no_result))
            return
        }

        _repoList.value = list.items
    }

    fun loadMoreRepoListData() {
        viewModelScope.launch(exceptionHandler) {
            val list = repoService.getSearchedRepoList(searchedWord, ++page, PER_PAGE).toLocalModel()
            _repoList.value = _repoList.value?.plus(list.items)
        }
    }

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PER_PAGE = 10
    }
}
