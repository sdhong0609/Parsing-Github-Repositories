package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel
import com.hongstudio.parsing_github_repositories.util.CommonMethod
import com.hongstudio.parsing_github_repositories.util.EventObserver
import com.hongstudio.parsing_github_repositories.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var repositoryItem: RepositoryItemModel? = null
    private val detailViewModel: DetailViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repositoryItem) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        repositoryItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_REPOSTIROY, RepositoryItemModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_REPOSTIROY)
        }

        binding.apply {
            viewModel = detailViewModel
            lifecycleOwner = this@DetailActivity
            textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        detailViewModel.repoNullCheck()

        detailViewModel.error.observe(this, EventObserver { messageId ->
            CommonMethod.showToast(this, messageId)
        })
        detailViewModel.openRepository.observe(this, EventObserver {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(detailViewModel.repo?.repositoryUrl)
            startActivity(intent)
        })
    }

    companion object {
        private const val EXTRA_REPOSTIROY = "EXTRA_REPOSITORY"

        fun newIntent(context: Context, repositoryItem: RepositoryItemModel): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_REPOSTIROY, repositoryItem)
            return intent
        }
    }
}
