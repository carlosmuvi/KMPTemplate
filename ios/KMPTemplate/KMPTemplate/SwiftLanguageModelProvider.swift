import Foundation
import FoundationModels
import KMPTemplateKit

/// Swift implementation of LanguageModelProvider that uses Apple Intelligence
@available(iOS 26.0, *)
class SwiftLanguageModelProvider: LanguageModelProvider {
    func __runModel(instructions: String, prompt: String, completionHandler: @escaping @Sendable (String?, (any Error)?) -> Void) {
        Task {
            do {
                let result = try await runModel(instructions: instructions, prompt: prompt)
                completionHandler(result, nil)
            } catch {
                completionHandler(nil, error)
            }
        }
    }

    func runModel(instructions: String, prompt: String) async throws -> String {
        let session = LanguageModelSession(instructions: instructions)
        let response = try await session.respond(to: prompt)
        return response.content
    }
}
