package com.acuf5928.marvelcharacters.network.interceptor

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class TryCatchInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(418)
                .message("Errore mentre comunicavo con il server")
                .body("{${e}}".toResponseBody(null)).build()
        }
}
