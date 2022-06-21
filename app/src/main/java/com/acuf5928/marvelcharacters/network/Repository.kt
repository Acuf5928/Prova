package com.acuf5928.marvelcharacters.network

import com.acuf5928.marvelcharacters.model.local.ErrorModel
import com.acuf5928.marvelcharacters.model.local.Model
import com.acuf5928.marvelcharacters.model.remote.GetCharactersModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val service: ApiService
) {
    suspend fun getCharacters(): Model = withContext(Dispatchers.IO) {
        try {
            var getCharactersModel: GetCharactersModel? = null

            while (true) {
                val offset = (getCharactersModel?.data?.offset ?: 0) + (getCharactersModel?.data?.count ?: 0)
                val response = service.getCharacters(offset = offset)
                val model = GeneraRequest.makeRequest(response)

                if (model is GetCharactersModel) {
                    if (getCharactersModel != null) {
                        model.data?.results = model.data?.results.orEmpty() + getCharactersModel.data?.results.orEmpty()
                    }
                    getCharactersModel = model

                    if(model.data?.count != 100) {
                        break
                    }
                } else {
                    return@withContext model
                }
            }

            getCharactersModel ?: ErrorModel(418, "Errore mentre comunicavo con il server")
        } catch (e: Exception) {
            e.printStackTrace()
            ErrorModel(418, "Errore mentre comunicavo con il server")
        }
    }
}