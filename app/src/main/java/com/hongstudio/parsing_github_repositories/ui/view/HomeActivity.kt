package com.hongstudio.parsing_github_repositories.ui.view

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
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.databinding.ActivityHomeBinding
import com.hongstudio.parsing_github_repositories.ui.viewmodel.HomeEvent
import com.hongstudio.parsing_github_repositories.ui.viewmodel.HomeViewModel
import com.hongstudio.parsing_github_repositories.util.extension.eventObserve
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

                    if (dy <= 0) return

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

        homeViewModel.repoList.observe(this) { list ->
            adapter.submitList(list)
        }

        homeViewModel.homeEvent.eventObserve(this) { event ->
            when (event) {
                is HomeEvent.Error -> showToast(event.messageRes)
                is HomeEvent.RepoItemClick -> startActivity(DetailActivity.newIntent(this, event.item))
                HomeEvent.HideKeyboard -> {
                    inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
                }

                HomeEvent.LastPage -> showToast(R.string.last_page)
            }
        }
    }

    private fun onRepoItemClick(item: RepoModel) {
        homeViewModel.onRepoItemClick(item)
    }
}
