import Foundation
import FoundationModels
import KMPTemplateKit

/// Apple Foundation Model implementation for iOS
/// Uses Apple Intelligence LanguageModelSession API
@available(iOS 26.0, *)
class AppleFoundationModel: AIModel {
    
    let id: String = "apple-foundation"
    let name: String = "Apple Foundation Model"
    let description_: String = "Apple's on-device foundation model for text generation and understanding"

    private let instructions: String = AIInstructions.shared.eventExtraction
    
    func __run(prompt: String) async throws -> Any? {
        Task {
            return try await runLanguageModel(prompt: prompt)
        }
    }
    
    func __run(prompt: String, completionHandler: @escaping @Sendable (AIModelResponse?, (any Error)?) -> Void) {
        Task {
            do {
                let response = try await runLanguageModel(prompt: prompt)
                completionHandler(response, nil)
            } catch {
                completionHandler(nil, error)
            }
        }
    }

    private func runLanguageModel(prompt: String) async throws -> AIModelResponse {
        let session = LanguageModelSession(instructions: instructions)
        let response = try await session.respond(to: prompt)
        let aiResponse = AIModelResponse(
            text: response.content,
            finishReason: FinishReason.stop
        )
        return aiResponse
    }
}
