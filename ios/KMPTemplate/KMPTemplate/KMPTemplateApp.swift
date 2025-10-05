import SwiftUI
import KMPTemplateKit

@main
struct KMPTemplateApp: App {
    init() {
        KoinKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
        }
    }
}
