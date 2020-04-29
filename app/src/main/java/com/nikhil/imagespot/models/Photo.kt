package com.nikhil.imagespot.models

import com.google.gson.annotations.SerializedName

class PhotoResponse(
    @SerializedName("stat")
    val stat: String,
    @SerializedName("photos")
    val photosData: PhotosData
)

class PhotosData (
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("perpage")
    val perpage: Int,
    @SerializedName("total")
    val total: String,
    @SerializedName("photo")
    val photosList: List<Photo>
)

class Photo(
    @SerializedName("farm")
    val farm: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("isfamily")
    val isFamily: Int,
    @SerializedName("isfriend")
    val isFriend: Int,
    @SerializedName("ispublic")
    val isPublic: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("server")
    val server: String,
    @SerializedName("title")
    val title: String
)