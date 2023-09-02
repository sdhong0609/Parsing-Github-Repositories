package com.hongstudio.parsing_github_repositories.util.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hongstudio.parsing_github_repositories.util.Event
import com.hongstudio.parsing_github_repositories.util.EventObserver

fun <T> LiveData<Event<T>>.eventObserve(
    lifecycleOwner: LifecycleOwner,
    onEventUnhandledContent: (T) -> Unit,
) {
    observe(lifecycleOwner, EventObserver {
        onEventUnhandledContent(it)
    })
}
