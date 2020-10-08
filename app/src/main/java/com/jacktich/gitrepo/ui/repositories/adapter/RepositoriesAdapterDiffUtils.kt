package com.jacktich.gitrepo.ui.repositories.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem

class RepositoriesAdapterDiffUtils : DiffUtil.ItemCallback<RepositoriesItem>() {

    override fun areItemsTheSame(oldItem: RepositoriesItem, newItem: RepositoriesItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: RepositoriesItem, newItem: RepositoriesItem): Boolean =
        oldItem == newItem

}