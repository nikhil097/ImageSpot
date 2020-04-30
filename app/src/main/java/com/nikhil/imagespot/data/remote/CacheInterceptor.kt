package com.nikhil.imagespot.data.remote

import com.nikhil.imagespot.ImageSpotApplication
import com.nikhil.imagespot.utils.hasNetwork
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = if (hasNetwork(ImageSpotApplication.instance)!!)
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        else
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()

        // Add the modified request to the chain.
       return chain.proceed(request)
    }

}