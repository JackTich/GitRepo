package com.jacktich.gitrepo.data.preferences

interface PreferencesHelper {

    fun setAccessToken(token: String)

    fun getAccessToken(): String?

    fun clearAccessToken()
}