package com.acuf5928.marvelcharacters.network

import com.acuf5928.marvelcharacters.BuildConfig
import com.acuf5928.marvelcharacters.Config
import com.acuf5928.marvelcharacters.network.interceptor.AuthenticatorInterceptor
import com.acuf5928.marvelcharacters.network.interceptor.LoggingInterceptor
import com.acuf5928.marvelcharacters.network.interceptor.TryCatchInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ProxySelector
import java.util.concurrent.TimeUnit

class ApiClient(
    private val environment: Config,
    private val loggingInterceptor: LoggingInterceptor,
    private val authenticatorInterceptor: AuthenticatorInterceptor,
    private val tryCatchInterceptor: TryCatchInterceptor,
) {

    private val okHttpClient by lazy { OkHttpClient() }
    private val connectionTimeout: Long = 30
    private val writeTimeout: Long = 30
    private val readTimeout: Long = 30

    private val client by lazy {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 4

        val builder = okHttpClient.newBuilder()
        builder.connectTimeout(connectionTimeout, TimeUnit.SECONDS)
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS)
        builder.readTimeout(readTimeout, TimeUnit.SECONDS)
        builder.dispatcher(dispatcher)

        if (BuildConfig.DEBUG) {
            builder.proxySelector(ProxySelector.getDefault())
            builder.addNetworkInterceptor(loggingInterceptor.loggingInterceptor)
        }
        builder.addNetworkInterceptor(tryCatchInterceptor)
        builder.addNetworkInterceptor(authenticatorInterceptor)


        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder()
            .baseUrl(environment.baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
        builder.client(client).build()
    }

    fun <T> createService(tClass: Class<T>): T = retrofit.create(tClass)
}