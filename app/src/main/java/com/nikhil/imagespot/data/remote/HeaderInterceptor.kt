package com.nikhil.imagespot.data.remote

import android.util.Log
import com.nikhil.imagespot.BuildConfig
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        val url = request.url().newBuilder()
            .addQueryParameter("format", "json")
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .addQueryParameter("nojsoncallback", "1")
            .build()

        Log.v("InterceptorHeader", "format")

        val requestBuilder = request.newBuilder().url(url).build();

        return chain.proceed(requestBuilder)
    }
}