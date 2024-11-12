package com.debugdesk.bot.di

import android.app.Application
import com.debugdesk.bot.di.KoinModule.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class BotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BotApp)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}