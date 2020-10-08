package com.jacktich.gitrepo.data.api.responces

import com.google.gson.annotations.SerializedName

data class SearchRepositoriesResponse(
    @SerializedName("items")
    val items: List<RepositoriesItem>
)