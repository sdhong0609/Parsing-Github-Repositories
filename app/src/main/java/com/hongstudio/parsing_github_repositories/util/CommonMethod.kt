package com.hongstudio.parsing_github_repositories.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object CommonMethod {
    fun showToast(context: Context, @StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }
}
