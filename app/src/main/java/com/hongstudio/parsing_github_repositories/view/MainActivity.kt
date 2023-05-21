package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.adapter.RepositoryRecyclerViewAdapter
import com.hongstudio.parsing_github_repositories.databinding.ActivityMainBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import com.hongstudio.parsing_github_repositories.service.RetrofitClient.githubRepositoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity(), HomeScreenEventAction {
    private lateinit var binding: ActivityMainBinding
    private lateinit var inputMethodManager: InputMethodManager
    private val adapter = RepositoryRecyclerViewAdapter(::onRepositoryItemClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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

        binding.recyclerViewRepositories.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
        }

    }

    override fun onSearchButtonClick() {
        searchRepositoriesAction()
    }

    private fun searchRepositoriesAction() {
        val keyword = binding.editTextSearch.text.toString().trim()
        if (keyword.isEmpty()) {
            showToast(R.string.please_input_keyword)
            return
        }
        binding.apply {
            inputMethodManager.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            wifiImageVisible = false
            progressBarVisible = true
            recyclerViewVisible = false
            loadRepositoriesData(keyword)
        }
    }

    private fun loadRepositoriesData(keyword: String) {
        val call = githubRepositoryService.getSearchedRepositoryList(keyword)
        call.enqueue(object : Callback<RepositoryListModel> {
            override fun onResponse(
                call: Call<RepositoryListModel>,
                response: Response<RepositoryListModel>
            ) {
                onLoadRepositoriesSuccess(response)
            }

            override fun onFailure(call: Call<RepositoryListModel>, t: Throwable) {
                binding.progressBarVisible = false
                when (t) {
                    is IOException -> {
                        binding.wifiImageVisible = true
                        showToast(R.string.there_is_no_interent)
                    }

                    else -> {
                        showToast(R.string.something_wrong_happened)
                    }
                }
            }

        })
    }

    private fun onLoadRepositoriesSuccess(response: Response<RepositoryListModel>) {
        binding.progressBarVisible = false

        if (!response.isSuccessful) {
            showToast(R.string.something_wrong_happened)
            return
        }

        val searchedResult = response.body()
        if (searchedResult?.items == null) {
            showToast(R.string.something_wrong_happened)
            return
        }

        if (searchedResult.items.isEmpty()) {
            showToast(R.string.there_is_no_result)
            return
        }

        adapter.submitList(searchedResult.items)
        binding.recyclerViewVisible = true
    }

    private fun onRepositoryItemClick(item: RepositoryItemModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repositoryItem", item)
        startActivity(intent)
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

}
