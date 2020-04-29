package com.nikhil.imagespot

import android.content.Context
import com.nikhil.imagespot.inject.DaggerAppComponent
import com.nikhil.imagespot.inject.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ImageSpotApplication: DaggerApplication() {

    val TAG = ImageSpotApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var instance: ImageSpotApplication
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .networkModule(NetworkModule(BuildConfig.API_BASE_URL))
            .build()
        appComponent.inject(this)
        return appComponent
    }

}