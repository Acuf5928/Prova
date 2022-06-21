package com.acuf5928.marvelcharacters.model.local

import com.google.gson.annotations.SerializedName


data class ErrorModel(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
) : Model()