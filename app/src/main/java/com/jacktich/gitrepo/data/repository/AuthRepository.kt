package com.jacktich.gitrepo.data.repository

import com.google.gson.JsonObject
import com.jacktich.gitrepo.BuildConfig
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.data.api.requests.LoginRequest
import com.jacktich.gitrepo.data.preferences.PreferencesHelper
import com.jacktich.gitrepo.utils.ApiConst
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: IGitApi,
    private var preferencesHelper: PreferencesHelper
) {
    fun postAuthRequest(code: String): Flowable<JsonObject> {
        return api.doServerPostTokenRequest(
            ApiConst.URL_GET_ACCESS_TOKEN,
            LoginRequest(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                code = code
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                it.printStackTrace()
            }
    }

    fun putTokenInPref(token: String) = preferencesHelper.setAccessToken(token)
}