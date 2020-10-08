package com.jacktich.gitrepo.data.api.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("code")
    val code: String
)