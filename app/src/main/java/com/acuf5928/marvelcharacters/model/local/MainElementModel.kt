package com.acuf5928.marvelcharacters.model.local

import androidx.annotation.Keep
import java.io.Serializable

data class ListMainElementModel(
    val totalElement: Int,
    val downloadedElement: Int,
    val mainElementModel: List<MainElementModel>,
): Model() {
    @Keep
    data class MainElementModel(
        val id: String = "",
        val imgLink: String = "",
        val title: String = "",
        val description: String = ""
    ) : Serializable
}