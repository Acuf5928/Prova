package com.acuf5928.marvelcharacters.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acuf5928.marvelcharacters.model.local.ErrorModel
import com.acuf5928.marvelcharacters.model.local.MainElementModel
import com.acuf5928.marvelcharacters.model.remote.GetCharactersModel
import com.acuf5928.marvelcharacters.network.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ViewModelHome(
    private val repository: Repository,
    private val sharedPref: SharedPreferences,
    private val gson: Gson
) : ViewModel() {
    private val _result = MutableLiveData<List<MainElementModel>?>(listOf())
    val result: LiveData<List<MainElementModel>?> = _result

    private var loading = false

    val isError = MutableLiveData<ErrorModel?>(null)

    fun getCharacters() {
        viewModelScope.launch {
            val cache = sharedPref.getString("CACHE", "")
            if (!cache.isNullOrBlank()) {
                try {
                    val mCache: GetCharactersModel = gson.fromJson(cache, GetCharactersModel::class.java)

                    _result.postValue(mCache.data?.results?.map {
                        MainElementModel(
                            imgLink = (it.thumbnail?.path.orEmpty() + "/standard_medium." + it.thumbnail?.extension.orEmpty()).replace(
                                "http://",
                                "https://"
                            ),
                            title = it.name.orEmpty(),
                            description = it.description.orEmpty()
                        )
                    })
                } catch (E: Exception) {
                    //do nothings
                }
            }
            if (_result.value.isNullOrEmpty() && !loading) {
                loading = true
                repository.getCharacters().let { response ->
                    when (response) {
                        is GetCharactersModel -> {
                            sharedPref.edit().putString("CACHE", gson.toJson(response)).apply()

                            _result.postValue(response.data?.results?.map {
                                MainElementModel(
                                    imgLink = (it.thumbnail?.path.orEmpty() + "/standard_medium." + it.thumbnail?.extension.orEmpty()).replace(
                                        "http://",
                                        "https://"
                                    ),
                                    title = it.name.orEmpty(),
                                    description = it.description.orEmpty()
                                )
                            })
                        }
                        is ErrorModel -> isError.value = response
                    }

                    loading = false
                }
            }
        }
    }
}