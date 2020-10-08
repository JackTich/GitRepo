package com.jacktich.gitrepo.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jacktich.gitrepo.BuildConfig
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.data.api.apihelpers.NetworkBoundResource
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.api.requests.LoginRequest
import com.jacktich.gitrepo.data.api.responces.ErrorAuthResponse
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.api.responces.TokenResponse
import com.jacktich.gitrepo.data.preferences.PreferencesHelper
import com.jacktich.gitrepo.utils.ApiConst
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: IGitApi,
    private val context: Context,
    private var preferencesHelper: PreferencesHelper
) {
    fun postAuthRequest(code: String): LiveData<Resource<Any>> {
        return object : NetworkBoundResource<Any>() {
            override fun createCall(): LiveData<Resource<Any>> {
                val publisher = api.doServerPostTokenRequest(
                    ApiConst.URL_GET_ACCESS_TOKEN,
                    LoginRequest(
                        clientId = BuildConfig.CLIENT_ID,
                        clientSecret = BuildConfig.CLIENT_SECRET,
                        code = code
                    )
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map {
                        if (Gson().fromJson(it, TokenResponse::class.java).accessToken != null) {
                            Resource.success(Gson().fromJson(it, TokenResponse::class.java))
                        } else {
                            Resource.success(Gson().fromJson(it, ErrorAuthResponse::class.java))
                        }
                    }
                    .onErrorReturnItem(Resource.error(context.getString(R.string.error_connection)))
                return LiveDataReactiveStreams.fromPublisher(publisher)
            }
        }.asLiveData()
    }

    fun putTokenInPref(token: String) = preferencesHelper.setAccessToken(token)

    fun isTokenExist(): Boolean = preferencesHelper.getAccessToken() != null
}