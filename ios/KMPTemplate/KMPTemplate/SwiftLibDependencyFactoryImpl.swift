import Foundation
import KMPTemplateKit
import UIKit

/**
 * Swift implementation of the dependency factory.
 *
 * This class provides Swift-only dependencies to the Kotlin Multiplatform shared code.
 * Use this for Swift Package Manager dependencies or Swift-only frameworks.
 *
 * Note: CalendarManager is NOT here because it's implemented in Kotlin (iOSMain)
 * and uses EventKit directly. It's provided via the iOS platformModule.
 */
class SwiftLibDependencyFactoryImpl: SwiftLibDependencyFactory {
    static let shared = SwiftLibDependencyFactoryImpl()

    private init() {}

    func providePlatformInfo() -> any PlatformInfo {
        return PlatformInfoImpl()
    }

    func provideAIModels() -> AIModels {
        var models: [any AIModel] = []

        // Add Apple Foundation Model if available (iOS 26+)
        if #available(iOS 26.0, *) {
            models.append(AppleFoundationModel())
        }

        // Add more Swift-only AI models here as needed:
        // models.append(CustomModel())

        return AIModels(models: models)
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
