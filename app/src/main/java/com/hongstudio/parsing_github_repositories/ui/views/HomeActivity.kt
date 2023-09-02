package com.hongstudio.parsing_github_repositories.ui.views

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.data.remote.RepoModel
import com.hongstudio.parsing_github_repositories.databinding.ActivityHomeBinding
import com.hongstudio.parsing_github_repositories.ui.viewmodels.HomeViewModel
import com.hongstudio.parsing_github_repositories.util.EventObserver
import com.hongstudio.parsing_github_repositories.util.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var inputMethodManager: InputMethodManager
    private val adapter = RepoListAdapter(::onRepoItemClick)
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeActivity
        }

        binding.repoRecyclerView.apply {
            adapter = this@HomeActivity.adapter
            addItemDecoration(DividerItemDecoration(this@HomeActivity, LinearLayoutManager.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                    if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                        homeViewModel.loadMoreRepoListData()
                    }
                }
            })
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
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

        homeViewModel.hideKeyboardEvent.observe(this, EventObserver {
            inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
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
