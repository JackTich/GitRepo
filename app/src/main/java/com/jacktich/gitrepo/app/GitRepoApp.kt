package com.jacktich.gitrepo.app

import android.app.Application
import com.jacktich.gitrepo.di.components.DaggerAppComponents
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class GitRepoApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponents.builder()
            .application(this)
            .build()
            .inject(this)
    }

}