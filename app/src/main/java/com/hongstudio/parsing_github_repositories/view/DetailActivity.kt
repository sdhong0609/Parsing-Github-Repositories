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
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding
import com.hongstudio.parsing_github_repositories.model.RepoModel
import com.hongstudio.parsing_github_repositories.util.EventObserver
import com.hongstudio.parsing_github_repositories.util.showToast
import com.hongstudio.parsing_github_repositories.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        // binding 초기화
        binding.apply {
            viewModel = detailViewModel
            lifecycleOwner = this@DetailActivity
            textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        // 데이터 파싱
        val repositoryItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_REPOSTIROY, RepoModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_REPOSTIROY)
        }

        // LiveData 구독
        detailViewModel.error.observe(this, EventObserver { messageId ->
            showToast(messageId)
        })
        detailViewModel.openRepoUrlEvent.observe(this, EventObserver { url ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        })

        detailViewModel.init(repositoryItem)
    }

    companion object {
        private const val EXTRA_REPOSTIROY = "EXTRA_REPOSITORY"

        fun newIntent(context: Context, repoItem: RepoModel): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_REPOSTIROY, repoItem)
            return intent
        }
    }
}
