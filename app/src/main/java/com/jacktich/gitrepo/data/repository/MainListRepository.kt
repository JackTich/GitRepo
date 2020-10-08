package com.jacktich.gitrepo.data.repository

import android.content.Context
import android.widget.Toast
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.api.responces.SearchRepositoriesResponse
import com.jacktich.gitrepo.data.preferences.PreferencesHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainListRepository @Inject constructor(
    private val api: IGitApi,
    private val preferencesHelper: PreferencesHelper
) {

    fun getPublicRepositories(since: Long?): Flowable<List<RepositoriesItem>> {
        return api.doServerGetAllRepos(since)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getSearchRepositories(title: String): Flowable<SearchRepositoriesResponse> {
        return api.doServerGetSearchRepos(title)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun isAuthorizing(): Boolean = preferencesHelper.getAccessToken() != null

    fun getUserRepositories(): Flowable<List<RepositoriesItem>> {
        return api.doServerGetUserRepos(preferencesHelper.getAccessToken()!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun clearAccessToken() = preferencesHelper.clearAccessToken()

}