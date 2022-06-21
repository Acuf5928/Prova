package com.acuf5928.marvelcharacters.network.interceptor

import com.acuf5928.marvelcharacters.Config
import okhttp3.Interceptor
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


class AuthenticatorInterceptor(
    private val environment: Config
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val date = Date().time.toString()

        val original = chain.request()
        val originalHttpUrl = original.url

        val md5 = MessageDigest.getInstance("MD5")
        md5.reset()

        val hash = BigInteger(
            1,
            md5.digest("$date${environment.privateKey}${environment.publicKey}".toByteArray())
        )
            .toString(16)
            .padStart(32, '0')

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("ts", date)
            .addQueryParameter("apikey", environment.publicKey)
            .addQueryParameter("hash", hash)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        return chain.proceed(requestBuilder.build())
    }
}