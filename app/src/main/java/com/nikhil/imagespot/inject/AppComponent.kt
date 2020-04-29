package com.nikhil.imagespot.inject

import android.app.Application
import com.nikhil.imagespot.ImageSpotApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, NetworkModule::class, ActivityBindingModule::class, ViewModelModule::class])
interface AppComponent : AndroidInjector<ImageSpotApplication> {

    override fun inject(instance: ImageSpotApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent

        fun networkModule(networkModule: NetworkModule): Builder
    }
}