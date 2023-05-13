package com.hongstudio.parsing_github_repositories.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.databinding.ItemRecyclerBinding
import com.hongstudio.parsing_github_repositories.model.ItemModel
import com.hongstudio.parsing_github_repositories.view.DetailActivity

class RepositoryRecyclerViewAdapter(private val itemList: List<ItemModel>) :
    RecyclerView.Adapter<RepositoryRecyclerViewAdapter.RepositoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val binding =
            ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RepositoryItemViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemModel) {
            binding.apply {
                Glide.with(itemView).load(item.owner.ownerImageUrl).into(imageViewOwner)
                textViewRepositoryName.text = item.repositoryName
                textViewOwnerName.text = itemView.context.getString(R.string.owner_with_value, item.owner.ownerName)
                textViewFork.text = itemView.context.getString(R.string.fork_with_value, item.forksCount)
                textViewWatchers.text = itemView.context.getString(R.string.watcher_with_value, item.watchersCount)
                textViewStars.text = itemView.context.getString(R.string.star_with_value, item.starsCount)
                textViewDescription.text = item.repositoryDescription
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("ownerImageUrl", item.owner.ownerImageUrl)
                intent.putExtra("repositoryName", item.repositoryName)
                intent.putExtra("ownerName", item.owner.ownerName)
                intent.putExtra("forksCount", item.forksCount)
                intent.putExtra("watchersCount", item.watchersCount)
                intent.putExtra("starsCount", item.starsCount)
                intent.putExtra("repositoryDescription", item.repositoryDescription)
                intent.putExtra("repositoryUrl", item.repositoryUrl)
                itemView.context.startActivity(intent)
            }
        }
    }
}