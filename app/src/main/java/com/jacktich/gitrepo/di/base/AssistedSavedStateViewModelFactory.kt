package com.jacktich.gitrepo.di.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface AssistedSavedStateViewModelFactory<VM: ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): VM
}