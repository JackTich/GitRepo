package com.jacktich.gitrepo.data.api.responces

import com.google.gson.annotations.SerializedName

data class RepositoriesItem (
    val isShimmer: Boolean? = null,
    @SerializedName("id")
    val id: Long,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("owner")
    val owner: DataOwner,
    @SerializedName("description")
    val description: String
)
data class DataOwner(
    @SerializedName("avatar_url")
    val avatar_url: String?
)