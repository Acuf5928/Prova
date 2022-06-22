package com.acuf5928.marvelcharacters.network

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.acuf5928.marvelcharacters.model.local.ListMainElementModel
import com.acuf5928.marvelcharacters.model.local.Model
import com.acuf5928.marvelcharacters.model.remote.ErrorModel
import com.acuf5928.marvelcharacters.model.remote.GetCharactersModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers

class Repository(
    private val service: ApiService,
    private val gson: Gson,
    private val sharedPref: SharedPreferences
) {
    private var _resultGetCharacters: ListMainElementModel? = null
    private var loadingGetCharacters = false
    private var getCharacters: LiveData<Model?>? = null

    fun getCharacters(invalidate: Boolean = false) = if (invalidate || getCharacters == null) {
        _resultGetCharacters = null
        loadingGetCharacters = false
        getCharacters = null

        getCharacters = liveData(Dispatchers.IO) {
            try {
                val cache = sharedPref.getString("CACHE", "")
                val mCache: GetCharactersModel? = try {
                    gson.fromJson(cache, GetCharactersModel::class.java)
                } catch (E: Exception) {
                    null
                }

                if (mCache != null && !mCache.data?.results.isNullOrEmpty()) {
                    try {
                        emit(mapModel(mCache))
                    } catch (E: Exception) {
                        //do nothings
                    }
                }

                var getCharactersModel: GetCharactersModel? = null

                if (_resultGetCharacters == null) {
                    if (!loadingGetCharacters) {
                        loadingGetCharacters = true

                        while (true) {
                            val offset = (getCharactersModel?.data?.offset ?: 0) + (getCharactersModel?.data?.count ?: 0)
                            val response = service.getCharacters(offset = offset)
                            val model = GeneraRequest.makeRequest(response)

                            if (model is GetCharactersModel) {
                                if (getCharactersModel != null) {
                                    model.data?.results = model.data?.results.orEmpty() + getCharactersModel.data?.results.orEmpty()
                                }
                                getCharactersModel = model

                                _resultGetCharacters = mapModel(model)

                                if (!loadingGetCharacters) {
                                    break
                                }

                                if (mCache == null || mCache.data?.results.isNullOrEmpty()) {
                                    emit(_resultGetCharacters)
                                }

                                if (model.data?.count != 100) {
                                    sharedPref.edit().putString("CACHE", gson.toJson(model)).apply()
                                    emit(_resultGetCharacters)
                                    break
                                }
                            } else {
                                emit(model)

                                if (mCache != null && !mCache.data?.results.isNullOrEmpty()) {
                                    try {
                                        emit(mapModel(mCache))
                                    } catch (E: Exception) {
                                        //do nothings
                                    }
                                }

                                break
                            }
                        }
                        loadingGetCharacters = false
                    }
                } else {
                    emit(_resultGetCharacters)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ErrorModel("418", "Errore mentre comunicavo con il server"))

                val cache = sharedPref.getString("CACHE", "")
                val mCache: GetCharactersModel? = try {
                    gson.fromJson(cache, GetCharactersModel::class.java)
                } catch (E: Exception) {
                    null
                }

                if (mCache != null && !mCache.data?.results.isNullOrEmpty()) {
                    try {
                        emit(mapModel(mCache))
                    } catch (E: Exception) {
                        //do nothings
                    }
                }
            }
        }

        getCharacters
    } else {
        getCharacters
    }

    private fun mapModel(mCache: GetCharactersModel) =
        ListMainElementModel(
            totalElement = 0,
            downloadedElement = 0,
            mainElementModel =
            mCache.data?.results?.map {
                ListMainElementModel.MainElementModel(
                    imgLink = it
                        .thumbnail
                        ?.path
                        .orEmpty()
                        .plus("/standard_medium.")
                        .plus(it.thumbnail?.extension.orEmpty())
                        .replace("http://", "https://"),
                    title = it.name.orEmpty(),
                    description = it.description.orEmpty()
                )
            }.orEmpty()
        )
}
