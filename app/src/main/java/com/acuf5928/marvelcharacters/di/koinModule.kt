package com.acuf5928.marvelcharacters.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.databinding.ktx.BuildConfig
import com.acuf5928.marvelcharacters.Config
import com.acuf5928.marvelcharacters.network.ApiClient
import com.acuf5928.marvelcharacters.network.ApiService
import com.acuf5928.marvelcharacters.network.Repository
import com.acuf5928.marvelcharacters.network.interceptor.AuthenticatorInterceptor
import com.acuf5928.marvelcharacters.network.interceptor.LoggingInterceptor
import com.acuf5928.marvelcharacters.network.interceptor.TryCatchInterceptor
import com.acuf5928.marvelcharacters.viewmodel.ViewModelHome
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level
import org.koin.dsl.module

val singleElement = module {
    single { Gson() }
    single { Config() }
    single { ApiClient(get(), get(), get(), get()) }
    single {
        get<Context>().getSharedPreferences("settings", MODE_PRIVATE)
    }
}

val networkInterceptor = module {
    single { LoggingInterceptor() }
    single { TryCatchInterceptor() }
    single { AuthenticatorInterceptor(get()) }
}

val services = module {
    single { get<ApiClient>().createService(ApiService::class.java) }
    single { Repository(get(), get(), get()) }
}

val singleViewModels = module {
    single { ViewModelHome(get()) }
}

val viewModels = module {

}


fun startKoin(context: Context) {
    org.koin.core.context.startKoin {
        when {
            BuildConfig.DEBUG -> androidLogger(Level.ERROR)
            else -> EmptyLogger()
        }

        androidContext(context)

        modules(
            listOf(
                viewModels,
                singleViewModels,
                singleElement,
                networkInterceptor,
                services
            )
        )
    }
}