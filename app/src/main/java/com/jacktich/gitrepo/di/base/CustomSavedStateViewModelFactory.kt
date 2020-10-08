package com.jacktich.gitrepo.di.base

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.Reusable
import javax.inject.Inject
import javax.inject.Provider

@Reusable
class CustomSavedStateViewModelFactory @Inject constructor(
    private val assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>
) {
    fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                assistedFactories[modelClass]?.let {
                    try {
                        return it.create(handle) as T
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                } ?: throw IllegalArgumentException("Unknown model class $modelClass")
            }
        }
    }
}
