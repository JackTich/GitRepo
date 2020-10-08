package com.jacktich.gitrepo.ui.repositories

import android.content.Context
import androidx.lifecycle.*
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.repository.MainListRepository
import com.jacktich.gitrepo.di.base.AssistedSavedStateViewModelFactory
import com.jacktich.gitrepo.utils.ApiConst
import com.jacktich.gitrepo.utils.Extensions
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.Disposable

class RepositoriesViewModel @AssistedInject constructor(
    private val listRepositories: MainListRepository,
    private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<RepositoriesViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): RepositoriesViewModel
    }

    val liveRepositoriesItem = MutableLiveData<List<RepositoriesItem>>()
    val  allUserRepositories = MutableLiveData<List<RepositoriesItem>>()
    val currentRepositoryCount = MutableLiveData(ApiConst.PAGE_COUNT.toLong())
    val etFieldText = MutableLiveData<String>()

    private lateinit var disposableSearch: Disposable
    init {
        if (isAuthorizing()){
            getUserRepositories()
        }else{
            getPublicRepositories()
        }
    }

    fun getPublicRepositories(since: Long? = null) {
        cancelPreviousSearch()
         listRepositories.getPublicRepositories(since).subscribe({
             if (liveRepositoriesItem.value == null){
                 liveRepositoriesItem.value = it
             }else{
                 liveRepositoriesItem.value = liveRepositoriesItem.value!! + it
             }
         },{
             it.printStackTrace()
             Extensions.showServerError(context)
         })
    }

    fun isAuthorizing(): Boolean = listRepositories.isAuthorizing()

    fun getUserRepositories(){
        listRepositories.getUserRepositories().subscribe({
            allUserRepositories.value = it
            liveRepositoriesItem.value = it
        }, {
            it.printStackTrace()
            Extensions.showServerError(context)
        })
    }

    fun getSearchRepositories(title: String) {
        cancelPreviousSearch()
        disposableSearch = listRepositories.getSearchRepositories(title).subscribe({
            liveRepositoriesItem.value = it.items
        }, {
            it.printStackTrace()
            Extensions.showServerError(context)
        })
    }

    fun clearToken() = listRepositories.clearAccessToken()

    fun cancelPreviousSearch(){
        if (this::disposableSearch.isInitialized && !disposableSearch.isDisposed){
            disposableSearch.dispose()
        }
    }

}