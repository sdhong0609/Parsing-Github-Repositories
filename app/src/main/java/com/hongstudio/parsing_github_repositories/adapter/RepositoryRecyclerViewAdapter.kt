package com.hongstudio.parsing_github_repositories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hongstudio.parsing_github_repositories.databinding.ItemRepositoryBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryItemModel

class RepositoryRecyclerViewAdapter(private val onClickRepositoryItem: (RepositoryItemModel) -> Unit) :
    ListAdapter<RepositoryItemModel, RepositoryRecyclerViewAdapter.RepositoryItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = RepositoryItemViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                onClickRepositoryItem(item)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepositoryItemViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepositoryItemModel) {
            binding.item = item
            Glide.with(itemView).load(item.owner.ownerImageUrl).into(binding.imageViewOwner)
        }
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<RepositoryItemModel>() {
    override fun areItemsTheSame(oldItem: RepositoryItemModel, newItem: RepositoryItemModel): Boolean {
        return oldItem.repositoryUrl == newItem.repositoryUrl
    }

    override fun areContentsTheSame(oldItem: RepositoryItemModel, newItem: RepositoryItemModel): Boolean {
        return oldItem == newItem
    }

}