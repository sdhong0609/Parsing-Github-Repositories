package com.hongstudio.parsing_github_repositories.view

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.apply {
            repositoryName = intent.getStringExtra("repositoryName")
            ownerName = intent.getStringExtra("ownerName")
            forksCount = intent.getIntExtra("forksCount", 0)
            watchersCount = intent.getIntExtra("watchersCount", 0)
            starsCount = intent.getIntExtra("starsCount", 0)
            repositoryDescription = intent.getStringExtra("repositoryDescription")
            repositoryUrl = intent.getStringExtra("repositoryUrl")
            detailActivity = this@DetailActivity

            Glide.with(this@DetailActivity).load(intent.getStringExtra("ownerImageUrl")).into(imageViewOwner)
            textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    val repositoryUrlClickListener = View.OnClickListener {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(binding.repositoryUrl)
        startActivity(intent)
    }
}