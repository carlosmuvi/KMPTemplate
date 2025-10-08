package dev.carlosmuvi.common

import dev.carlosmuvi.common.platform.CalendarManager
import org.koin.dsl.module

actual fun platformModule() = module {
    single { CalendarManager() }
}
