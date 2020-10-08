package com.jacktich.gitrepo.data.api.responces

import com.google.gson.annotations.SerializedName

data class ErrorRepositoriesResponse (
    @SerializedName("message")
    val message: String
)