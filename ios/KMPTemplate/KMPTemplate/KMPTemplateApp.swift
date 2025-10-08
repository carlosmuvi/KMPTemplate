import SwiftUI
import KMPTemplateKit

@main
struct KMPTemplateApp: App {
    init() {
        // Pass the AI models to Koin
        let aiModels: [AIModel]
        if #available(iOS 26.0, *) {
            aiModels = [AppleFoundationModel()]
        } else {
            aiModels = []
        }

        // Initialize Koin with AI models
        KoinKt.doInitKoin(aiModels: aiModels)
    }

    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
        }
    }
}
