package com.hongstudio.parsing_github_repositories.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.data.local.RepoModel
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding
import com.hongstudio.parsing_github_repositories.ui.viewmodel.DetailEvent
import com.hongstudio.parsing_github_repositories.ui.viewmodel.DetailViewModel
import com.hongstudio.parsing_github_repositories.util.extension.eventObserve
import com.hongstudio.parsing_github_repositories.util.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        binding.apply {
            viewModel = detailViewModel
            lifecycleOwner = this@DetailActivity
            repoUrlTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun observeViewModel() {
        detailViewModel.detailEvent.eventObserve(this) { event ->
            when (event) {
                is DetailEvent.Error -> showToast(event.messageRes)
                is DetailEvent.OpenRepoUrl -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.url)))
            }
        }
    }

    companion object {
        private const val REPO_KEY = "REPO_KEY"

        fun newIntent(context: Context, repoItem: RepoModel): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(REPO_KEY, repoItem)
            return intent
        }
    }
}
