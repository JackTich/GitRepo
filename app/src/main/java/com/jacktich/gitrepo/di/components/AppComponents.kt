package com.jacktich.gitrepo.di.components

import android.app.Application
import com.jacktich.gitrepo.app.GitRepoApp
import com.jacktich.gitrepo.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponents {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponents

    }

    fun inject(app: GitRepoApp)
}