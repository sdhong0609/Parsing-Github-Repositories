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
import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import com.hongstudio.parsing_github_repositories.service.GithubRepositoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repositoryRecyclerViewAdapter: RepositoryRecyclerViewAdapter
    private val linearLayoutManager = LinearLayoutManager(this@MainActivity)
    private val dividerItemDecoration by lazy { DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL) }
    private val inputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivity = this@MainActivity

        binding.editTextSearch.apply {
            setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    loadRepositoriesData(text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    val buttonSearchClickListener = View.OnClickListener {
        loadRepositoriesData(binding.editTextSearch.text.toString())
    }

    private fun loadRepositoriesData(keyword: String) {
        if (keyword.trim().isNotEmpty()) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val githubRepositoryService: GithubRepositoryService =
                retrofit.create(GithubRepositoryService::class.java)

            val searchedRepositoryListCall = githubRepositoryService.getSearchedRepositoryList(keyword)

            searchedRepositoryListCall.enqueue(object : Callback<RepositoryListModel> {
                override fun onResponse(
                    call: Call<RepositoryListModel>,
                    response: Response<RepositoryListModel>
                ) {
                    if (response.isSuccessful) {
                        val searchedResult = response.body()
                        if (searchedResult?.items != null) {
                            repositoryRecyclerViewAdapter = RepositoryRecyclerViewAdapter { itemModel ->
                                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                                intent.putExtra("ownerImageUrl", itemModel.owner.ownerImageUrl)
                                intent.putExtra("repositoryName", itemModel.repositoryName)
                                intent.putExtra("ownerName", itemModel.owner.ownerName)
                                intent.putExtra("forksCount", itemModel.forksCount)
                                intent.putExtra("watchersCount", itemModel.watchersCount)
                                intent.putExtra("starsCount", itemModel.starsCount)
                                intent.putExtra(
                                    "repositoryDescription",
                                    itemModel.repositoryDescription
                                )
                                intent.putExtra("repositoryUrl", itemModel.repositoryUrl)
                                this@MainActivity.startActivity(intent)
                            }

                            binding.recyclerViewRepositories.apply {
                                adapter = repositoryRecyclerViewAdapter
                                layoutManager = linearLayoutManager
                                addItemDecoration(dividerItemDecoration)
                            }
                            repositoryRecyclerViewAdapter.submitList(searchedResult.items)
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.something_wrong_happened),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RepositoryListModel>, t: Throwable) {
                    call.cancel()
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.something_wrong_happened),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.please_input_keyword),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}