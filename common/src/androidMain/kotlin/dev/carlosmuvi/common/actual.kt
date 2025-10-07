package dev.carlosmuvi.common

import dev.carlosmuvi.common.platform.CalendarManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { CalendarManager(androidContext()) }
}