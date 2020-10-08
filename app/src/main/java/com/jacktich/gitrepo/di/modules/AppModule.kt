package com.jacktich.gitrepo.di.modules

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jacktich.gitrepo.BuildConfig
import com.jacktich.gitrepo.app.GitRepoApp
import com.jacktich.gitrepo.data.api.IGitApi
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.data.preferences.AppPreferencesHelper
import com.jacktich.gitrepo.data.preferences.PreferencesHelper
import com.jacktich.gitrepo.utils.ApiConst
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ActivityModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideGitRepoApi(): IGitApi {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient =
            OkHttpClient.Builder().callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
        val converterFactory = GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(converterFactory)
            .client(httpClient.build())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(IGitApi::class.java)
    }

    @Singleton
    @Provides
    fun providePreferences(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper =
        appPreferencesHelper
}