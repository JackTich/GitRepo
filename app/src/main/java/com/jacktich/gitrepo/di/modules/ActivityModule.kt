package com.jacktich.gitrepo.di.modules

import com.jacktich.gitrepo.ui.auth.AuthActivity
import com.jacktich.gitrepo.ui.repositories.RepositoriesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeRepositoriesActivity(): RepositoriesActivity

}