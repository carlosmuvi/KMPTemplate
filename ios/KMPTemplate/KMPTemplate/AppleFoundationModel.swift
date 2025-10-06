import Foundation
import FoundationModels
import KMPTemplateKit

/// Apple Foundation Model implementation for iOS
/// Uses Apple Intelligence LanguageModelSession API
@available(iOS 26.0, *)
class AppleFoundationModel: AIModel {
    func __run(prompt: String) async throws -> Any? {
        Task {
            let response = try await runLanguageModel(prompt: prompt)
            let aiResponse = AIModelResponse(
                text: response,
                finishReason: FinishReason.stop
            )
            return aiResponse
        }
    }
    
    func __run(prompt: String, completionHandler: @escaping @Sendable (AIModelResponse?, (any Error)?) -> Void) {
        Task {
            do {
                let response = try await runLanguageModel(prompt: prompt)
                let aiResponse = AIModelResponse(
                    text: response,
                    finishReason: FinishReason.stop
                )
                completionHandler(aiResponse, nil)
            } catch {
                completionHandler(nil, error)
            }
        }
    }

    
    let id: String = "apple-foundation"
    let name: String = "Apple Foundation Model"
    let description_: String = "Apple's on-device foundation model for text generation and understanding"

    private let instructions: String

    init() {
        self.instructions = AIInstructions.shared.eventExtraction
    }

    private func runLanguageModel(prompt: String) async throws -> String {
        let session = LanguageModelSession(instructions: instructions)
        let response = try await session.respond(to: prompt)
        return response.content
    }
}
