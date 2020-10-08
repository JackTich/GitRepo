package com.jacktich.gitrepo.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.data.api.apihelpers.NetworkBoundResource
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.preferences.PreferencesHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainListRepository @Inject constructor(
    private val api: IGitApi,
    private val context: Context,
    private val preferencesHelper: PreferencesHelper
) {
    fun getPublicRepositories(since: Long?): LiveData<Resource<List<RepositoriesItem>>> {
        return object : NetworkBoundResource<List<RepositoriesItem>>() {
            override fun createCall(): LiveData<Resource<List<RepositoriesItem>>> {
                val publisher = api.doServerGetAllRepos(since)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map {Resource.success(it)}
                    .onErrorReturnItem(Resource.error(context.getString(R.string.error_connection)))
                return LiveDataReactiveStreams.fromPublisher(publisher)
            }
        }.asLiveData()
    }

    fun isAuthorizing(): Boolean = preferencesHelper.getAccessToken() != null

    fun getUserRepositories(): Flowable<List<RepositoriesItem>> {
        return api.doServerGetUserRepos(preferencesHelper.getAccessToken()!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                it.printStackTrace()
                Toast.makeText(
                    context,
                    context.getString(R.string.error_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getSearchRepositories(title: String): Flowable<List<RepositoriesItem>> {
        return api.doServerGetSearchRepos(title)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                it.printStackTrace()
                Toast.makeText(
                    context,
                    context.getString(R.string.error_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}