package com.jacktich.gitrepo.di.modules

import androidx.lifecycle.ViewModel
import com.jacktich.gitrepo.di.base.AssistedSavedStateViewModelFactory
import com.jacktich.gitrepo.di.base.ViewModelKey
import com.jacktich.gitrepo.ui.auth.AuthViewModel
import com.jacktich.gitrepo.ui.repositories.RepositoriesViewModel
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(RepositoriesViewModel::class)
    abstract fun bindRepositoriesViewModel(repositoriesViewModel: RepositoriesViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

}