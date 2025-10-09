package dev.carlosmuvi.common.platform

/**
 * Simple example interface for demonstrating Swift dependency integration.
 *
 * This can be replaced with actual interfaces for real Swift dependencies
 * like Analytics, Firebase, etc.
 */
interface PlatformInfo {
    /**
     * Returns the name of the platform (e.g., "iOS", "Android")
     */
    fun getPlatformName(): String

    /**
     * Returns the OS version
     */
    fun getOSVersion(): String
}