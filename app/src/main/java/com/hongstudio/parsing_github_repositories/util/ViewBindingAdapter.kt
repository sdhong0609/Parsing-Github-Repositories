package com.hongstudio.parsing_github_repositories.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(url: String?) {
    Glide.with(this).load(url).error(android.R.color.transparent).into(this)
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

