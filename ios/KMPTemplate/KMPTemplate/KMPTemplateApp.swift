import SwiftUI
import KMPTemplateKit

@main
struct KMPTemplateApp: App {
    init() {
        // Pass the AI models to Koin
        if #available(iOS 26.0, *) {
            KoinKt.doInitKoin(aiModels: [AppleFoundationModel()])
        } else {
            KoinKt.doInitKoin(aiModels: [])
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
