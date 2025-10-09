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

    func provideCalendarManager() -> CalendarManager {
        // CalendarManager is an actual class in Kotlin that uses EventKit
        // We instantiate it directly since it's already iOS-specific
        return CalendarManager()
    }

    func provideAIModels() -> [any AIModel] {
        var models: [any AIModel] = []

        // Add Apple Foundation Model if available (iOS 26+)
        if #available(iOS 26.0, *) {
            models.append(AppleFoundationModel())
        }

        // Add more AI models here as needed:
        // models.append(CustomModel())

        return models
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
