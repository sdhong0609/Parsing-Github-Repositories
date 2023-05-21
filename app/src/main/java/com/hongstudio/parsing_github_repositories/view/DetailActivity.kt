package com.hongstudio.parsing_github_repositories.view

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
                repo = repositoryItem
                detailScreenEventAction = this@DetailActivity
                textViewRepositoryUrl.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        } else {
            binding.noDataImageVisible = true
            showToast(R.string.failed_load_data)
        }
    }

    override fun onRepositoryLinkClick(url: String) {
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            showToast(R.string.wrong_web_url)
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
