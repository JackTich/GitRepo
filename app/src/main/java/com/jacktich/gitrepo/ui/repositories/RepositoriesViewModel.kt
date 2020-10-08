package com.jacktich.gitrepo.ui.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jacktich.gitrepo.data.api.RepositoryParser
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.repository.MainListRepository
import javax.inject.Inject

class RepositoriesViewModel @Inject constructor(
    private val listRepositories: MainListRepository
) : ViewModel() {

    fun getPublicRepositories(since: Long? = null): LiveData<Resource<List<RepositoriesItem>>> {
        return listRepositories.getPublicRepositories(since)
    }

    fun isAuthorizing(): Boolean = listRepositories.isAuthorizing()

    fun getUserRepositories(): LiveData<List<RepositoriesItem>> {
        return LiveDataReactiveStreams.fromPublisher(listRepositories.getUserRepositories())
    }

    fun getSearchRepositories(title: String): LiveData<List<RepositoriesItem>> {
        return LiveDataReactiveStreams.fromPublisher(listRepositories.getSearchRepositories(title))
    }
}