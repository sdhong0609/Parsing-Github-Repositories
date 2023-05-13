package com.hongstudio.parsing_github_repositories.view

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val ownerImageUrl = intent.getStringExtra("ownerImageUrl")
        val repositoryName = intent.getStringExtra("repositoryName")
        val ownerName = intent.getStringExtra("ownerName")
        val forksCount = intent.getIntExtra("forksCount", 0)
        val watchersCount = intent.getIntExtra("watchersCount", 0)
        val starsCount = intent.getIntExtra("starsCount", 0)
        val repositoryDescription = intent.getStringExtra("repositoryDescription")
        val repositoryUrl = intent.getStringExtra("repositoryUrl")

        binding.apply {
            Glide.with(this@DetailActivity).load(ownerImageUrl).into(imageViewOwner)
            textViewRepositoryName.text = repositoryName
            textViewOwnerName.text = getString(R.string.owner_with_value, ownerName)
            textViewFork.text = getString(R.string.fork_with_value, forksCount)
            textViewWatchers.text = getString(R.string.watcher_with_value, watchersCount)
            textViewStars.text = getString(R.string.star_with_value, starsCount)
            textViewDescription.text = repositoryDescription
            textViewRepositoryUrl.text = repositoryUrl
        }

        binding.textViewRepositoryUrl.apply {
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(repositoryUrl)
                startActivity(intent)
            }
        }
    }
}