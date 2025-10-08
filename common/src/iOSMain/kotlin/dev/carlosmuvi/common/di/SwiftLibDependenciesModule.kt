package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.platform.PlatformInfo
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Creates a Koin module for Swift dependencies.
 *
 * This module binds Swift-only implementations to their Kotlin interfaces,
 * making them injectable throughout the KMP codebase.
 *
 * @param factory The Swift implementation of SwiftLibDependencyFactory
 * @return A Koin module containing all Swift dependencies
 */
internal fun swiftLibDependenciesModule(factory: SwiftLibDependencyFactory): Module = module {
    // Bind PlatformInfo from Swift implementation
    single<PlatformInfo> { factory.providePlatformInfo() }

    // Add more Swift dependencies here as needed:
    // single<Analytics> { factory.provideAnalytics() }
    // single<SomeOtherSwiftLib> { factory.provideSomeOtherSwiftLib() }
}

/**
 * Extension function to provide the Swift dependency factory to Koin.
 *
 * Usage in Swift:
 * ```swift
 * KoinKt.doInitKoin { koinApp in
 *     koinApp.provideSwiftLibDependencyFactory(
 *         factory: SwiftLibDependencyFactoryImpl.shared
 *     )
 * }
 * ```
 */
fun KoinApplication.provideSwiftLibDependencyFactory(factory: SwiftLibDependencyFactory): KoinApplication {
    modules(swiftLibDependenciesModule(factory))
    return this
}