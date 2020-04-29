package com.nikhil.imagespot.data.remote

class DataWrapper<T>(var response: T? = null, var error: Throwable? = null, var isLoading: Boolean = false) {

}