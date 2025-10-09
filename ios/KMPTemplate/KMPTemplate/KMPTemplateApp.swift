import SwiftUI
import KMPTemplateKit

@main
struct KMPTemplateApp: App {
    init() {
        // Initialize Koin with Swift dependency factory
        KoinKt.doInitKoin { koinApp in
            koinApp.provideSwiftLibDependencyFactory(
                factory: SwiftLibDependencyFactoryImpl.shared
            )
        }
    }
    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
        }
    }
}
