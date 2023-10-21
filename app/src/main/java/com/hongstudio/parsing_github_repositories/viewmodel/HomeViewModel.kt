package com.hongstudio.parsing_github_repositories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import com.hongstudio.parsing_github_repositories.service.GithubRepositoryService
import com.hongstudio.parsing_github_repositories.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeViewModel(private val repoService: GithubRepositoryService) : ViewModel() {
    private val _wifiImageVisible = MutableLiveData(false)
    val wifiImageVisible: LiveData<Boolean> get() = _wifiImageVisible

    private val _progressBarVisible = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean> get() = _progressBarVisible

    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>> get() = _error

    private val _repositoryList = MutableLiveData<List<RepositoryItemModel>>()
    val repositoryList: LiveData<List<RepositoryItemModel>> get() = _repositoryList

    private val _hideKeyboard = MutableLiveData<Event<Unit>>()
    val hideKeyboard: LiveData<Event<Unit>> get() = _hideKeyboard

    private val _repositoryItemClickEvent = MutableLiveData<Event<RepositoryItemModel>>()
    val repositoryItemClickEvent: LiveData<Event<RepositoryItemModel>> get() = _repositoryItemClickEvent

    val keyword = MutableLiveData("")

    fun onRepositoryItemClick(item: RepositoryItemModel) {
        _repositoryItemClickEvent.value = Event(item)
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
        _repositoryList.value = emptyList()

        loadRepositoriesData(searchedWord)
    }

    private fun loadRepositoriesData(keyword: String) {
        val call = repoService.getSearchedRepositoryList(keyword)
        call.enqueue(object : Callback<RepositoryListModel> {
            override fun onResponse(
                call: Call<RepositoryListModel>,
                response: Response<RepositoryListModel>
            ) {
                onLoadRepositoriesSuccess(response)
            }

            override fun onFailure(call: Call<RepositoryListModel>, t: Throwable) {
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

        })
    }

    private fun onLoadRepositoriesSuccess(response: Response<RepositoryListModel>) {
        _progressBarVisible.value = false

        if (!response.isSuccessful) {
            _error.value = Event(R.string.something_wrong_happened)
            return
        }

        val searchedResult = response.body()
        if (searchedResult?.items == null) {
            _error.value = Event(R.string.something_wrong_happened)
            return
        }

        if (searchedResult.items.isEmpty()) {
            _error.value = Event(R.string.there_is_no_result)
            return
        }

        _repositoryList.value = searchedResult.items
    }
}