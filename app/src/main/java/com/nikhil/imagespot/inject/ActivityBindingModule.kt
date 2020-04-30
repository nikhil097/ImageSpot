package com.nikhil.imagespot.inject

import com.nikhil.imagespot.ui.home.HomeActivity
import com.nikhil.imagespot.ui.imagePreview.ImagePreviewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun imagePreviewActivity(): ImagePreviewActivity

}