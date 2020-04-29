package com.nikhil.imagespot.inject

import android.content.Context
import com.nikhil.imagespot.ImageSpotApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: ImageSpotApplication): Context {
        return application.applicationContext
    }
}