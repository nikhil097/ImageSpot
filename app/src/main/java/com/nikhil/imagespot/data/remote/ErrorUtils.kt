package com.nikhil.imagespot.data.remote

import com.google.gson.Gson

object ErrorUtils {
    fun <T> parseError(json: String, responseClass: Class<T>?): T {
        return Gson().fromJson(json, responseClass)
    }
}
