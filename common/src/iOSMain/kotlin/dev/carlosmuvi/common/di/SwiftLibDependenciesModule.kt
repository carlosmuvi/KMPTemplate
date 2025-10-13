package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.model.AIModels
import dev.carlosmuvi.common.platform.PlatformInfo
import org.koin.core.KoinApplication
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Creates a Koin module for Swift-only dependencies.
 *
 * This module binds Swift implementations to their Kotlin interfaces,
 * making them injectable throughout the KMP codebase.
 *
 * @param factory The Swift implementation of SwiftLibDependencyFactory
 * @return A Koin module containing all Swift dependencies
 */
internal fun swiftLibDependenciesModule(factory: SwiftLibDependencyFactory) = module {
    // Platform information (uses UIKit APIs)
    single { factory.providePlatformInfo() } bind PlatformInfo::class

    // List of available AI models (Swift-only implementations)
    single { factory.provideAIModels() } bind AIModels::class
}

/**
 * Extension function to provide Swift dependency factory to Koin.
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