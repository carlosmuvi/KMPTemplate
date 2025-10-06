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

                TextEditor(text: $inputText)
                    .frame(height: 120)
                    .padding(8)
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(Color(.systemGray4), lineWidth: 1)
                    )
                    .onChange(of: inputText) { _, newValue in
                        viewModel.updateInputText(text: newValue)
                    }
            }
            .padding(.horizontal)

            // Parse Button
            Button(action: {
                viewModel.parseEvent()
            }) {
                Observing(viewModel.eventState) { state in
                    HStack {
                        if state is UiStateLoading {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle(tint: .white))
                                .scaleEffect(0.8)
                        }
                        Text(state is UiStateLoading ? "Parsing..." : "Parse Event")
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
            Observing(viewModel.eventState) { state in
                VStack(spacing: 16) {
                    if let successState = state as? UiStateSuccess<Event> {
                        EventCardView(event: successState.data!)

                        Button(action: {
                            viewModel.confirmEvent()
                        }) {
                            Text("Add to Calendar")
                                .fontWeight(.semibold)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.green)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                        }
                        .padding(.horizontal)
                    } else if let errorState = state as? UiStateError {
                        VStack(spacing: 12) {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .font(.system(size: 40))
                                .foregroundColor(.red)

                            Text("Error")
                                .font(.headline)
                                .foregroundColor(.red)

                            Text(errorState.message)
                                .font(.body)
                                .foregroundColor(.secondary)
                                .multilineTextAlignment(.center)
                                .padding(.horizontal)

                            Button(action: {
                                viewModel.resetState()
                            }) {
                                Text("Try Again")
                                    .fontWeight(.semibold)
                                    .padding(.horizontal, 32)
                                    .padding(.vertical, 12)
                                    .background(Color.blue)
                                    .foregroundColor(.white)
                                    .cornerRadius(8)
                            }
                        }
                        .padding()
                        .background(Color(.systemGray6))
                        .cornerRadius(12)
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

                if let reminder = event.reminder {
                    EventDetailRow(
                        icon: "bell",
                        title: "Reminder",
                        value: formatReminder(reminder)
                    )
                }
            }
        }
        .padding()
        .background(Color(.systemGray6))
        .cornerRadius(12)
        .padding(.horizontal)
    }

    private func formatDateTime(_ dateTime: LocalDateTime) -> String {
        return "\(dateTime.year)-\(String(format: "%02d", dateTime.monthNumber))-\(String(format: "%02d", dateTime.dayOfMonth)) at \(String(format: "%02d", dateTime.hour)):\(String(format: "%02d", dateTime.minute))"
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
