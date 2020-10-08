package com.jacktich.gitrepo.ui.auth

import androidx.lifecycle.*
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.repository.AuthRepository
import com.jacktich.gitrepo.di.base.AssistedSavedStateViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class AuthViewModel @AssistedInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory: AssistedSavedStateViewModelFactory<AuthViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): AuthViewModel
    }

    fun getAccessToken(code: String): LiveData<Resource<Any>> {
       return authRepository.postAuthRequest(code)
    }

    fun putTokenInPref(token: String) = authRepository.putTokenInPref(token)

    fun isTokenExist(): Boolean = authRepository.isTokenExist()

}