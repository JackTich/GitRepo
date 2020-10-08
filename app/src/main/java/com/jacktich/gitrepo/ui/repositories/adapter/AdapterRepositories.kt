package com.jacktich.gitrepo.ui.repositories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.squareup.picasso.Picasso

class AdapterRepositories() :
    ListAdapter<RepositoriesItem, RecyclerView.ViewHolder>(RepositoriesAdapterDiffUtils()) {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_SHIMMER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_NORMAL) {
            RepositoriesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
            )
        } else {
            ShimmerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shimmer_repository, parent, false)
            )
        }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).isShimmer != null) TYPE_SHIMMER else TYPE_NORMAL

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (getItemViewType(position) == TYPE_NORMAL)
            (holder as RepositoriesViewHolder).bindRepositoriesViewHolder(getItem(position))
        else (holder as ShimmerViewHolder).bindShimmerViewHolder()

    inner class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shimmerRepositoryItem =
            itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerRepositoryItem)

        fun bindShimmerViewHolder() {
            shimmerRepositoryItem.startShimmer()
        }
    }

    inner class RepositoriesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imgAvatarAuthor = itemView.findViewById<ImageView>(R.id.imgAvatarAuthor)
        private val tvTitleRep = itemView.findViewById<TextView>(R.id.tvTitleRep)
        private val tvDescriptionRep = itemView.findViewById<TextView>(R.id.tvDescriptionRep)

        fun bindRepositoriesViewHolder(repository: RepositoriesItem) {

            if (repository.owner.avatar_url != null) {
                Picasso.get()
                    .load(repository.owner.avatar_url)
                    .error(ContextCompat.getDrawable(itemView.context, R.drawable.ic_github)!!)
                    .into(imgAvatarAuthor)
            } else {
                imgAvatarAuthor.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_github
                    )!!
                )
            }

            tvTitleRep.text = repository.fullName
            tvDescriptionRep.text = repository.description

        }

    }

    fun getLatestItemId(): Long = getItem(itemCount - 1).id

}