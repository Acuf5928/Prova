package com.acuf5928.marvelcharacters.network

import com.acuf5928.marvelcharacters.model.remote.GetCharactersModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<GetCharactersModel>
}