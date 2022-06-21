package com.acuf5928.marvelcharacters

import android.app.Application
import com.acuf5928.marvelcharacters.di.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this)
    }
}