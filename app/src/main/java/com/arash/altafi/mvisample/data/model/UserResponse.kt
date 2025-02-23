package com.arash.altafi.mvisample.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("family")
    val family: String,
    @SerializedName("avatar")
    val avatar: String,
) : Parcelable