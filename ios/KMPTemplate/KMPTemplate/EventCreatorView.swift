import SwiftUI
import KMPTemplateKit

struct EventCreatorView: View {
    @StateObject var viewModelStoreOwner = IOSViewModelStoreOwner()
    @State private var inputText: String = ""

    var body: some View {
        let viewModel: EventCreatorViewModel = viewModelStoreOwner.viewModel()

        VStack(spacing: 24) {
            Text("Event Creator")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding(.top)

            Text("Enter event details and AI will extract the information")
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            // Input Text Field
            VStack(alignment: .leading, spacing: 8) {
                Text("Event Details")
                    .font(.headline)

                if #available(iOS 17.0, *) {
                    TextEditor(text: $inputText)
                        .frame(height: 120)
                        .padding(8)
                        .background(Color(.systemGray6))
                        .cornerRadius(8)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color(.systemGray4), lineWidth: 1)
                        )
                        .onChange(of: inputText) {
                            viewModel.updateInputText(text: inputText)
                        }
                } else {
                    // Fallback on earlier versions
                }
            }
            .padding(.horizontal)

            // Parse Button
            Button(action: {
                viewModel.parseEvent()
            }) {
                Observing(viewModel.state) { state in
                    HStack {
                        if state.isLoading {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle(tint: .white))
                                .scaleEffect(0.8)
                        }
                        Text(state.isLoading ? "Parsing..." : "Parse Event")
                            .fontWeight(.semibold)
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(inputText.isEmpty ? Color.gray : Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(10)
                }
            }
            .disabled(inputText.isEmpty)
            .padding(.horizontal)

            // Event State Display
            Observing(viewModel.state) { state in
                VStack(spacing: 16) {
                    // Error Display
                    if let error = state.error {
                        VStack(spacing: 12) {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .font(.system(size: state.event == nil ? 40 : 24))
                                .foregroundColor(.red)

                            Text(state.event == nil ? "Parsing Error" : "Calendar Error")
                                .font(.headline)
                                .foregroundColor(.red)

                            Text(error)
                                .font(.body)
                                .foregroundColor(.secondary)
                                .multilineTextAlignment(.center)
                                .padding(.horizontal)

                            Button(action: {
                                if state.event != nil {
                                    viewModel.confirmEvent()
                                } else {
                                    viewModel.resetState()
                                }
                            }) {
                                Text(state.event != nil ? "Retry" : "Try Again")
                                    .fontWeight(.semibold)
                                    .padding(.horizontal, 32)
                                    .padding(.vertical, 12)
                                    .background(Color.blue)
                                    .foregroundColor(.white)
                                    .cornerRadius(8)
                            }
                        }
                        .padding()
                        .background(state.event == nil ? Color(.systemGray6) : Color.red.opacity(0.1))
                        .cornerRadius(12)
                        .padding(.horizontal)
                    }

                    // Event Display
                    if let event = state.event {
                        EventCardView(event: event)

                        Button(action: {
                            viewModel.confirmEvent()
                        }) {
                            HStack {
                                if state.isAddingToCalendar {
                                    ProgressView()
                                        .progressViewStyle(CircularProgressViewStyle(tint: .white))
                                        .scaleEffect(0.8)
                                }
                                Text(state.isAddingToCalendar ? "Adding to Calendar..." : "Add to Calendar")
                                    .fontWeight(.semibold)
                            }
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(state.isAddingToCalendar ? Color.gray : Color.green)
                            .foregroundColor(.white)
                            .cornerRadius(10)
                        }
                        .disabled(state.isAddingToCalendar)
                        .padding(.horizontal)
                    }

                    // Success Message
                    if let successMessage = state.successMessage {
                        HStack {
                            Image(systemName: "checkmark.circle.fill")
                                .foregroundColor(.green)
                            Text(successMessage)
                                .fontWeight(.semibold)
                                .foregroundColor(.green)
                        }
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.green.opacity(0.1))
                        .cornerRadius(10)
                        .padding(.horizontal)
                    }
                }
            }

            Spacer()
        }
        .padding(.vertical)
    }
}

struct EventCardView: View {
    let event: Event

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Image(systemName: "calendar.badge.clock")
                    .font(.system(size: 24))
                    .foregroundColor(.blue)

                Text("Parsed Event")
                    .font(.headline)

                Spacer()

                Image(systemName: "checkmark.circle.fill")
                    .foregroundColor(.green)
            }

            Divider()

            VStack(alignment: .leading, spacing: 8) {
                EventDetailRow(icon: "text.bubble", title: "Title", value: event.title)

                if let description = event.description_ {
                    EventDetailRow(icon: "doc.text", title: "Description", value: description)
                }

                if let location = event.location {
                    EventDetailRow(icon: "location", title: "Location", value: location)
                }

                EventDetailRow(
                    icon: "calendar",
                    title: "Start",
                    value: formatDateTime(event.startDateTime)
                )

                EventDetailRow(
                    icon: "calendar",
                    title: "End",
                    value: formatDateTime(event.endDateTime)
                )

                if event.allDay {
                    EventDetailRow(icon: "sun.max", title: "All Day", value: "Yes")
                }
            }
        }
        .padding()
        .background(Color(.systemGray6))
        .cornerRadius(12)
        .padding(.horizontal)
    }

    private func formatDateTime(_ dateTime: LocalDateTime) -> String {
        return "\(dateTime.year)-\(String(format: "%02d", dateTime.month.number))-\(String(format: "%02d", dateTime.day)) at \(String(format: "%02d", dateTime.hour)):\(String(format: "%02d", dateTime.minute))"
    }

    private func formatReminder(_ reminder: ReminderTime) -> String {
        switch reminder {
        case .none, .atTime: return "At event time"
        case .fiveMinutes: return "5 minutes before"
        case .fifteenMinutes: return "15 minutes before"
        case .thirtyMinutes: return "30 minutes before"
        case .oneHour: return "1 hour before"
        case .oneDay: return "1 day before"
        }
    }
}

struct EventDetailRow: View {
    let icon: String
    let title: String
    let value: String

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            Image(systemName: icon)
                .frame(width: 20)
                .foregroundColor(.blue)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.caption)
                    .foregroundColor(.secondary)

                Text(value)
                    .font(.body)
            }
        }
    }
}
