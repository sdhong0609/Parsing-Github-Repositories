package com.hongstudio.parsing_github_repositories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hongstudio.parsing_github_repositories.databinding.ItemRepoBinding
import com.hongstudio.parsing_github_repositories.model.RepoModel

class RepoListAdapter(private val onRepoItemClick: (item: RepoModel) -> Unit) :
    ListAdapter<RepoModel, RepoListAdapter.RepoItemViewHolder>(RepoItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = RepoItemViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onRepoItemClick(getItem(position))
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RepoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepoItemViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepoModel) {
            binding.item = item
        }
    }
}

private class RepoItemDiffCallback : DiffUtil.ItemCallback<RepoModel>() {
    override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return oldItem.repoUrl == newItem.repoUrl
    }

    override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return oldItem == newItem
    }

}
