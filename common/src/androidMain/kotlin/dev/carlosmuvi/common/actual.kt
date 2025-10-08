package dev.carlosmuvi.common

import dev.carlosmuvi.common.platform.AndroidPlatformInfo
import dev.carlosmuvi.common.platform.PlatformInfo
import org.koin.dsl.module

actual fun platformModule() = module {
    // Add Android-specific dependencies here
    single<PlatformInfo> { AndroidPlatformInfo() }
}