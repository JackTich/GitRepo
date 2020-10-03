package com.jacktich.gitrepo.di.modules

import android.content.Context
import com.jacktich.gitrepo.app.GitRepoApp
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.utils.ApiConst
import dagger.Module
import dagger.Provides
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ActivityModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: GitRepoApp): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideGitRepoApi(): IGitApi = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(ApiConst.BASE_URL)
        .build()
        .create(IGitApi::class.java)

//    @Singleton
//    @Provides
//    fun providePreferences
}