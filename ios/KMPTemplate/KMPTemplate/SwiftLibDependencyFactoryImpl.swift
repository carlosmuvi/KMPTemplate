import Foundation
import KMPTemplateKit
import UIKit

/**
 * Swift implementation of the dependency factory.
 *
 * This class provides Swift-only dependencies to the Kotlin Multiplatform shared code.
 * Add new Swift dependencies here as needed (e.g., Firebase, native analytics, etc.)
 */
class SwiftLibDependencyFactoryImpl: SwiftLibDependencyFactory {
    static let shared = SwiftLibDependencyFactoryImpl()

    private init() {}

    func providePlatformInfo() -> any PlatformInfo {
        return PlatformInfoImpl()
    }
}

/**
 * Example implementation of PlatformInfo using UIKit APIs.
 * This demonstrates how to wrap Swift-only code for use in KMP.
 */
private class PlatformInfoImpl: PlatformInfo {
    func getPlatformName() -> String {
        return "iOS"
    }

    func getOSVersion() -> String {
        return UIDevice.current.systemVersion
    }
}
