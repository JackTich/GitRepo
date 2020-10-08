package com.jacktich.gitrepo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.gson.Gson
import com.jacktich.gitrepo.data.api.apihelpers.Resource
import com.jacktich.gitrepo.data.api.responces.ErrorAuthResponse
import com.jacktich.gitrepo.data.api.responces.ErrorRepositoriesResponse
import com.jacktich.gitrepo.data.api.responces.TokenResponse
import com.jacktich.gitrepo.data.repository.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getAccessToken(code: String): LiveData<Any> {

        return LiveDataReactiveStreams.fromPublisher(authRepository.postAuthRequest(code)).map {
            if (Gson().fromJson(it, TokenResponse::class.java).accessToken != null) {
                Gson().fromJson(it, TokenResponse::class.java)
            } else {
                Gson().fromJson(it, ErrorAuthResponse::class.java)
            }
        }

    }

    fun putTokenInPref(token: String) = authRepository.putTokenInPref(token)

}