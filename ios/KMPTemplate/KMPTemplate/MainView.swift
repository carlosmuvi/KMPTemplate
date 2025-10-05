import SwiftUI
import KMPTemplateKit

struct MainView: View {
    @StateObject var viewModelStoreOwner = IOSViewModelStoreOwner()

    var body: some View {
        let viewModel: MainViewModel = viewModelStoreOwner.viewModel()

        VStack(spacing: 20) {
            Observing(viewModel.message) { message in
                Text(message)
                    .font(.title)
                    .multilineTextAlignment(.center)
                    .padding()
            }

            Observing(viewModel.counter) { counter in
                Text("Counter: \(counter)")
                    .font(.system(size: 48, weight: .bold))
                    .padding()
            }

            HStack(spacing: 20) {
                Button(action: {
                    viewModel.decrementCounter()
                }) {
                    Text("-")
                        .font(.title)
                        .frame(width: 60, height: 60)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }

                Button(action: {
                    viewModel.incrementCounter()
                }) {
                    Text("+")
                        .font(.title)
                        .frame(width: 60, height: 60)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
            }
            .padding()

            Spacer()
                .frame(height: 40)

            VStack(alignment: .leading, spacing: 8) {
                Text("This is a Kotlin Multiplatform template with:")
                    .font(.headline)
                    .padding(.bottom, 4)

                Text("✓ Koin for dependency injection")
                Text("✓ ViewModel architecture")
                Text("✓ Jetpack Compose (Android)")
                Text("✓ SwiftUI (iOS)")
            }
            .padding()

            Spacer()
        }
        .padding()
    }
}
