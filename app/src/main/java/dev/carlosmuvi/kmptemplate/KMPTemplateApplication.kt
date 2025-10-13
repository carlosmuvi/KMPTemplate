package dev.carlosmuvi.kmptemplate

import android.app.Application
import co.touchlab.kermit.Logger
import dev.carlosmuvi.common.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class KMPTemplateApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@KMPTemplateApplication)
        }

        Logger.d { "KMPTemplateApplication" }
    }
}