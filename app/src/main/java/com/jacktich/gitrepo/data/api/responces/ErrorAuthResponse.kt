package com.jacktich.gitrepo.data.api.responces

import com.google.gson.annotations.SerializedName

data class ErrorAuthResponse(
    @SerializedName("error")
    val error: String
)