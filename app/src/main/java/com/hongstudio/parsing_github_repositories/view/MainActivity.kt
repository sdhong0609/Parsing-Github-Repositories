package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.adapter.RepositoryRecyclerViewAdapter
import com.hongstudio.parsing_github_repositories.databinding.ActivityMainBinding
import com.hongstudio.parsing_github_repositories.model.OwnerModel
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import com.hongstudio.parsing_github_repositories.service.RetrofitClient.githubRepositoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity(), HomeEventAction {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repositoryRecyclerViewAdapter: RepositoryRecyclerViewAdapter
    private lateinit var repositoryItem: RepositoryItemModel
    private val linearLayoutManager = LinearLayoutManager(this@MainActivity)
    private val dividerItemDecoration by lazy { DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL) }
    private val inputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            homeEventAction = this@MainActivity
            wifiImageVisible = false
            progressBarVisible = false
            recyclerViewVisible = false
        }

        binding.editTextSearch.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchRepositoriesAction()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onClickSearchButton(view: View) {
        searchRepositoriesAction()
    }

    private fun searchRepositoriesAction() {
        binding.apply {
            inputMethodManager.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            wifiImageVisible = false
            progressBarVisible = true
            recyclerViewVisible = false
            loadRepositoriesData(editTextSearch.text.toString())
        }
    }

    private fun loadRepositoriesData(keyword: String) {
        if (keyword.trim().isNotEmpty()) { // 검색창이 비어있지 않을 때
            val searchedRepositoryListCall = githubRepositoryService.getSearchedRepositoryList(keyword)

            searchedRepositoryListCall.enqueue(object : Callback<RepositoryListModel> {
                override fun onResponse(
                    call: Call<RepositoryListModel>,
                    response: Response<RepositoryListModel>
                ) {
                    if (response.isSuccessful) { // 응답이 성공했을 때
                        val searchedResult = response.body()
                        if (searchedResult?.items != null) { // 검색결과가 null이 아닐 때
                            if (searchedResult.items.isNotEmpty()) { // 검색결과가 존재할 때
                                repositoryRecyclerViewAdapter = RepositoryRecyclerViewAdapter { item ->
                                    repositoryItem = RepositoryItemModel(
                                        repositoryName = item.repositoryName,
                                        owner = OwnerModel(ownerName = item.owner.ownerName, ownerImageUrl = item.owner.ownerImageUrl),
                                        repositoryDescription = item.repositoryDescription,
                                        starsCount = item.starsCount,
                                        watchersCount = item.watchersCount,
                                        forksCount = item.forksCount,
                                        repositoryUrl = item.repositoryUrl
                                    )
                                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                                    intent.putExtra("repositoryItem", repositoryItem)
                                    this@MainActivity.startActivity(intent)
                                }

                                binding.recyclerViewRepositories.apply {
                                    adapter = repositoryRecyclerViewAdapter
                                    layoutManager = linearLayoutManager
                                    addItemDecoration(dividerItemDecoration)
                                }
                                repositoryRecyclerViewAdapter.submitList(searchedResult.items)
                                binding.recyclerViewVisible = true
                            } else { // 검색결과가 존재하지 않을 때
                                showToast(getString(R.string.there_is_no_result))
                            }
                        } else { // 검색결과가 null일 때
                            showToast(getString(R.string.something_wrong_happened))
                        }
                    } else { // 응답이 실패했을 때
                        showToast(getString(R.string.something_wrong_happened))
                    }
                    binding.progressBarVisible = false
                }

                override fun onFailure(call: Call<RepositoryListModel>, t: Throwable) {
                    binding.progressBarVisible = false
                    call.cancel()
                    when (t) {
                        is IOException -> {
                            binding.wifiImageVisible = true
                            showToast(getString(R.string.there_is_no_interent))
                        }
                        else -> {
                            showToast(getString(R.string.something_wrong_happened))
                        }
                    }
                }

            })
        } else { // 검색창이 비어있을 때
            binding.progressBarVisible = false
            showToast(getString(R.string.please_input_keyword))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

}