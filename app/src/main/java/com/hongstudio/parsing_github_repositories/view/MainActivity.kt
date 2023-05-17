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
        binding.homeEventAction = this@MainActivity

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
            imageViewWifiOff.visibility = View.GONE
            circularProgressBar.visibility = View.VISIBLE
            recyclerViewRepositories.visibility = View.INVISIBLE
            loadRepositoriesData(editTextSearch.text.toString())
        }
    }

    private fun loadRepositoriesData(keyword: String) {
        if (keyword.trim().isNotEmpty()) {
            val searchedRepositoryListCall = githubRepositoryService.getSearchedRepositoryList(keyword)

            searchedRepositoryListCall.enqueue(object : Callback<RepositoryListModel> {
                override fun onResponse(
                    call: Call<RepositoryListModel>,
                    response: Response<RepositoryListModel>
                ) {
                    if (response.isSuccessful) {
                        val searchedResult = response.body()
                        if (searchedResult?.items != null) {
                            if (searchedResult.items.isNotEmpty()) {
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
                                binding.recyclerViewRepositories.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.there_is_no_result),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.something_wrong_happened),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.something_wrong_happened),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.circularProgressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<RepositoryListModel>, t: Throwable) {
                    binding.circularProgressBar.visibility = View.GONE
                    call.cancel()
                    when (t) {
                        is IOException -> {
                            binding.imageViewWifiOff.visibility = View.VISIBLE
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.there_is_no_interent),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.something_wrong_happened),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            })
        } else {
            binding.circularProgressBar.visibility = View.GONE
            Toast.makeText(
                this@MainActivity,
                getString(R.string.please_input_keyword),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}