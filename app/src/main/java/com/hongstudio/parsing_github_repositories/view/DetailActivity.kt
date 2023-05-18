package com.hongstudio.parsing_github_repositories.view

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
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
            intent.getParcelableExtra("repositoryItem", RepositoryItemModel::class.java)
        } else {
            intent.getParcelableExtra("repositoryItem")
        }
        if (repositoryItem != null) {
            binding.apply {
                repositoryName = repositoryItem?.repositoryName
                ownerName = repositoryItem?.owner?.name
                forksCount = repositoryItem?.forksCount
                watchersCount = repositoryItem?.watchersCount
                starsCount = repositoryItem?.starsCount
                repositoryDescription = repositoryItem?.repositoryDescription
                repositoryUrl = repositoryItem?.repositoryUrl
                Glide.with(this@DetailActivity).load(repositoryItem?.owner?.imageUrl).into(imageViewOwner)

                detailScreenEventAction = this@DetailActivity
                textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        } else {
            binding.noDataImageVisible = true
            Toast.makeText(this, getString(R.string.failed_load_data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRepositoryLinkClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(binding.repositoryUrl)
        startActivity(intent)
    }
}