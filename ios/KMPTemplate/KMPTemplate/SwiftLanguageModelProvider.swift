import Foundation
import FoundationModels
import KMPTemplateKit

/// Swift implementation of LanguageModelProvider that uses Apple Intelligence
@available(iOS 26.0, *)
@objc(SwiftLanguageModelProvider)
class SwiftLanguageModelProvider: NSObject {

    @objc func runModel(instructions: String, prompt: String) async throws -> String {
        do {
            let session = LanguageModelSession(instructions: instructions)
            let response = try await session.respond(to: prompt)
            return response.content
        } catch {
            throw NSError(
                domain: "AppleIntelligence",
                code: -1,
                userInfo: [NSLocalizedDescriptionKey: error.localizedDescription]
            )
        }
    }
}
