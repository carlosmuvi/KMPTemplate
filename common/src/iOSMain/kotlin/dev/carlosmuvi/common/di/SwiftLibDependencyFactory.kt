package dev.carlosmuvi.common.di

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
     * Example: Provides a simple platform info implementation.
     * Replace or extend with your actual Swift dependencies.
     */
    fun providePlatformInfo(): PlatformInfo
}
