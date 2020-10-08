package com.jacktich.gitrepo.data.preferences

import android.content.Context
import com.jacktich.gitrepo.utils.AppConstants
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(private val context: Context): PreferencesHelper {

    companion object{
        const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    }

    private val prefCore = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)

    override fun setAccessToken(token: String) {
        prefCore.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
    }

    override fun getAccessToken(): String? = prefCore.getString(PREF_KEY_ACCESS_TOKEN, null)

    override fun clearAccessToken() = prefCore.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()


}