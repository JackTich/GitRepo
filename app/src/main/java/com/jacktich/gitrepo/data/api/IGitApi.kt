package com.jacktich.gitrepo.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.api.requests.LoginRequest
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.api.responces.TokenResponse
import com.jacktich.gitrepo.utils.ApiConst
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.*

interface IGitApi {

    @GET(ApiConst.URL_ALL_REPO)
    fun doServerGetAllRepos(@Query(ApiConst.PARAM_SINCE_ALL_REPO) since: Long?): Flowable<List<RepositoriesItem>>

    @POST
    fun doServerPostTokenRequest(
        @Url url: String = ApiConst.URL_GET_ACCESS_TOKEN,
        @Body loginRequest: LoginRequest,
        @Header("Accept") accept: String = "application/json"
    ): Flowable<JsonObject>

    @GET(ApiConst.URL_USER_REPO)
    fun doServerGetUserRepos(@Header("Authorization") accessToken: String): Flowable<List<RepositoriesItem>>

    @GET(ApiConst.URL_SEARCH_ALL_REPO)
    fun doServerGetSearchRepos(@Query(ApiConst.PARAM_TITLE_REPOS) title: String): Flowable<List<RepositoriesItem>>
}