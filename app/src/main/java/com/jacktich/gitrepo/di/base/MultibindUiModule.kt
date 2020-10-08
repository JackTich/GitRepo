package com.jacktich.gitrepo.di.base

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.multibindings.Multibinds

@Module
abstract class MultibindUiModule {
    @Multibinds
    abstract fun assistedViewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>
}