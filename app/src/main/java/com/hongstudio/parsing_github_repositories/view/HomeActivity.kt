package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.adapter.RepoListAdapter
import com.hongstudio.parsing_github_repositories.databinding.ActivityHomeBinding
import com.hongstudio.parsing_github_repositories.model.RepoModel
import com.hongstudio.parsing_github_repositories.service.RetrofitClient
import com.hongstudio.parsing_github_repositories.util.EventObserver
import com.hongstudio.parsing_github_repositories.util.showToast
import com.hongstudio.parsing_github_repositories.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var inputMethodManager: InputMethodManager
    private val adapter = RepoListAdapter(::onRepoItemClick)
    private val homeViewModel: HomeViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(RetrofitClient.githubRepositoryService) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeActivity
        }

        binding.recyclerViewRepositories.apply {
            adapter = this@HomeActivity.adapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(DividerItemDecoration(this@HomeActivity, LinearLayoutManager.VERTICAL))
        }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                homeViewModel.searchRepositoriesAction()
                true
            } else {
                false
            }
        }

        homeViewModel.error.observe(this, EventObserver { messageId ->
            showToast(messageId)
        })
        homeViewModel.repoList.observe(this) { list ->
            adapter.submitList(list)
        }
        homeViewModel.hideKeyboard.observe(this, EventObserver {
            inputMethodManager.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
        })
        homeViewModel.repoItemClickEvent.observe(this, EventObserver { item ->
            val intent = DetailActivity.newIntent(this, item)
            startActivity(intent)
        })
    }

    private fun onRepoItemClick(item: RepoModel) {
        homeViewModel.onRepoItemClick(item)
    }
}
