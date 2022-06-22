package com.acuf5928.marvelcharacters.model.remote

import com.acuf5928.marvelcharacters.model.local.Model
import com.google.gson.annotations.SerializedName


data class ErrorModel(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
) : Model()