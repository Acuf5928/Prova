package com.acuf5928.marvelcharacters.model.remote

import com.acuf5928.marvelcharacters.model.local.Model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class GetCharactersModel(
    @SerializedName("attributionHTML")
    val attributionHTML: String?,
    @SerializedName("attributionText")
    val attributionText: String?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("copyright")
    val copyright: String?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("status")
    val status: String?
) : Model() {
    data class Data(
        @SerializedName("count")
        val count: Int?,
        @SerializedName("limit")
        val limit: Int?,
        @SerializedName("offset")
        val offset: Int?,
        @SerializedName("results")
        var results: List<Result>?,
        @SerializedName("total")
        val total: Int?
    ) : Serializable

    data class Result(
        @SerializedName("comics")
        val comics: Element?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("events")
        val events: Element?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("modified")
        val modified: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("resourceURI")
        val resourceURI: String?,
        @SerializedName("series")
        val series: Element?,
        @SerializedName("stories")
        val stories: Element?,
        @SerializedName("thumbnail")
        val thumbnail: Thumbnail?,
        @SerializedName("urls")
        val urls: List<Url>?
    ) : Serializable

    data class Element(
        @SerializedName("available")
        val available: Int?,
        @SerializedName("collectionURI")
        val collectionURI: String?,
        @SerializedName("items")
        val items: List<Item>?,
        @SerializedName("returned")
        val returned: Int?
    ) : Serializable

    data class Thumbnail(
        @SerializedName("extension")
        val extension: String?,
        @SerializedName("path")
        val path: String?
    ) : Serializable

    data class Url(
        @SerializedName("type")
        val type: String?,
        @SerializedName("url")
        val url: String?
    ) : Serializable

    data class Item(
        @SerializedName("name")
        val name: String?,
        @SerializedName("resourceURI")
        val resourceURI: String?,
        @SerializedName("type")
        val type: String?
    ) : Serializable
}