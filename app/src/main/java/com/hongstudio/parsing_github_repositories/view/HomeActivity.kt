package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.adapter.RepositoryRecyclerViewAdapter
import com.hongstudio.parsing_github_repositories.databinding.ActivityHomeBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.util.EventObserver
import com.hongstudio.parsing_github_repositories.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity(), HomeScreenEventAction {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var inputMethodManager: InputMethodManager
    private val adapter = RepositoryRecyclerViewAdapter(::onRepositoryItemClick)
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeActivity
            homeEventAction = this@HomeActivity
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
            adapter = this@HomeActivity.adapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(DividerItemDecoration(this@HomeActivity, LinearLayoutManager.VERTICAL))
        }

        homeViewModel.error.observe(this, EventObserver { messageId ->
            showToast(messageId)
        })
        homeViewModel.repositoryList.observe(this) { repositoryList ->
            adapter.submitList(repositoryList)
        }

    }

    override fun onSearchButtonClick() {
        searchRepositoriesAction()
    }

    private fun searchRepositoriesAction() {
        val keyword = binding.editTextSearch.text.toString().trim()
        homeViewModel.searchRepositories(keyword)

        if (keyword.isNotEmpty()) {
            binding.apply {
                inputMethodManager.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            }
        }
    }

    private fun onRepositoryItemClick(item: RepositoryItemModel) {
        val intent = DetailActivity.newIntent(this, item)
        startActivity(intent)
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
