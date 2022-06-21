package com.acuf5928.marvelcharacters.network

import android.util.Log
import com.acuf5928.marvelcharacters.model.local.ErrorModel
import com.acuf5928.marvelcharacters.model.local.Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

object GeneraRequest {
    private const val TAG = "OkHttpRequests"

    fun <T> makeRequest(response: Response<T>): Model {
        Log.d(TAG, "#>>> Response code : ${response.code()}")
        return try {
            when {
                /** HTTPCODE 200 .. 299 */
                response.isSuccessful -> response.body() as Model
                else -> {
                    val bodyType = object : TypeToken<ErrorModel>() {}.type
                    Gson().fromJson<ErrorModel>(response.errorBody()!!.string(), bodyType)
                }
            }
        } catch (e: Exception) {
            ErrorModel(418, "Errore mentre comunicavo con il server")
        }
    }
}