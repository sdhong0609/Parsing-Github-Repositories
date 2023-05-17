package com.hongstudio.parsing_github_repositories.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}