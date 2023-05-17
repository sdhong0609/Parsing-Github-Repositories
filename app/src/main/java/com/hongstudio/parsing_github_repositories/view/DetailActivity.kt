package com.hongstudio.parsing_github_repositories.view

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel

class DetailActivity : AppCompatActivity(), DetailScreenEventAction {
    private lateinit var binding: ActivityDetailBinding
    private var repositoryItem: RepositoryItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.noDataImageVisible = false

        repositoryItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_REPOSITORY, RepositoryItemModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_REPOSITORY)
        }

        if (repositoryItem != null) {
            binding.apply {
                binding.repo = repositoryItem
                Glide.with(this@DetailActivity).load(repositoryItem?.owner?.ownerImageUrl).into(imageViewOwner)

                detailScreenEventAction = this@DetailActivity
                textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        } else {
            binding.noDataImageVisible = true
            Toast.makeText(this@DetailActivity, getString(R.string.failed_load_data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickRepositoryLink(url: String) {
        // TODO: url이 맞는지 검증
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    companion object {
        private const val EXTRA_REPOSITORY = "EXTRA_REPOSITORY"

        fun newIntent(context: Context, repositoryItem: RepositoryItemModel): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_REPOSITORY, repositoryItem)
            return intent
        }
    }
}