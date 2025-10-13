package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.model.AIModels
import dev.carlosmuvi.common.platform.PlatformInfo

/**
 * Factory interface for providing Swift-only dependencies to Kotlin Multiplatform.
 *
 * This allows integration of Swift Package Manager dependencies or Swift-only code
 * that cannot be directly accessed from Kotlin.
 *
 * Usage:
 * 1. Add a function here for each Swift dependency you want to expose
 * 2. Implement the factory in Swift (SwiftLibDependencyFactoryImpl.swift)
 * 3. Pass the Swift implementation to initKoin via provideSwiftLibDependencyFactory()
 *
 * Example Swift implementation:
 * ```swift
 * class SwiftLibDependencyFactoryImpl: SwiftLibDependencyFactory {
 *     func providePlatformInfo() -> PlatformInfo {
 *         return PlatformInfoImpl()
 *     }
 * }
 * ```
 */
interface SwiftLibDependencyFactory {
    /**
     * Provides platform information (OS version, etc.)
     */
    fun providePlatformInfo(): PlatformInfo

    /**
     * Provides available AI models (e.g., Apple Foundation Models)
     * These are Swift-only implementations that use frameworks like FoundationModels
     */
    fun provideAIModels(): AIModels
}
