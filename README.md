# Kotlin Multiplatform Template

A minimal Kotlin Multiplatform project template with essential architecture components for building cross-platform mobile applications.

## Features

This template includes:

- ✅ **Kotlin Multiplatform** - Share business logic between iOS and Android
- ✅ **Koin** - Dependency injection for all platforms
- ✅ **ViewModel** - Lifecycle-aware state management
- ✅ **Jetpack Compose** - Modern UI framework for Android
- ✅ **SwiftUI** - Native UI framework for iOS
- ✅ **SKIE** - Swift-friendly Kotlin API generation

## Project Structure

```
.
├── common/                 # Shared Kotlin code
│   ├── src/
│   │   ├── commonMain/    # Platform-agnostic code
│   │   ├── androidMain/   # Android-specific code
│   │   ├── iosMain/       # iOS-specific code
│   │   └── jvmMain/       # JVM-specific code
├── app/                   # Android application
└── ios/                   # iOS application (Xcode project)
```

## Getting Started

### Prerequisites

- **Android Development:**
  - Android Studio (latest version)
  - JDK 17+

- **iOS Development:**
  - Xcode 15+
  - macOS

### Running the Project

#### Android

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Run the `app` configuration

#### iOS

1. Open `ios/KMPTemplate/KMPTemplate.xcodeproj` in Xcode
2. Select your target device/simulator
3. Build and run (⌘R)

## Architecture

### Shared Module (`common`)

The shared module contains:

- **ViewModels** - Business logic and state management
- **Koin Modules** - Dependency injection configuration
- **Platform-specific implementations** - Using `expect`/`actual` mechanism

### Android App

- Uses Jetpack Compose for UI
- Integrates with Koin for dependency injection
- ViewModels are accessed via `koinViewModel()` composable

### iOS App

- Uses SwiftUI for UI
- Koin integration through the shared framework
- ViewModels are accessed through `IOSViewModelStoreOwner`

## Adding Features

### 1. Create a ViewModel

In `common/src/commonMain/kotlin/.../viewmodel/`:

```kotlin
class MyViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<MyState>(MyState.Loading)
    val state: StateFlow<MyState> = _state.asStateFlow()

    // Your business logic here
}
```

### 2. Register in Koin

In `common/src/commonMain/kotlin/.../di/ViewModelModule.kt`:

```kotlin
val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::MyViewModel)  // Add your ViewModel here
}
```

### 3. Use in Android (Compose)

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Your UI code
}
```

### 4. Use in iOS (SwiftUI)

```swift
struct MyView: View {
    @StateObject var viewModelStoreOwner = IOSViewModelStoreOwner()

    var body: some View {
        let viewModel: MyViewModel = viewModelStoreOwner.viewModel()

        Observing(viewModel.state) { state in
            // Your UI code
        }
    }
}
```

## Adding Dependencies

### Shared Dependencies

Add to `common/build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation("your.dependency:artifact:version")
}
```

### Platform-Specific Dependencies

```kotlin
androidMain.dependencies {
    implementation("android.specific:dependency:version")
}

iosMain.dependencies {
    implementation("ios.specific:dependency:version")
}
```

## Customization

### Renaming the Project

1. Update `rootProject.name` in `settings.gradle.kts`
2. Update iOS project name in Xcode
3. Update package names throughout the project
4. Update `applicationId` in `app/build.gradle.kts`
5. Update `namespace` in both module build files
6. Update the framework `baseName` in `common/build.gradle.kts`

### Changing the Theme

- **Android**: Edit `app/src/main/java/.../presentation/global/Theme.kt`
- **iOS**: Modify SwiftUI views directly or add a custom theme

## Using Swift Dependencies in Kotlin Multiplatform

This template includes a **SwiftLibDependencyFactory** pattern for integrating Swift-only dependencies (like Swift Package Manager libraries) into your Kotlin Multiplatform code.

### Why Use This Pattern?

Use this when you need to integrate:
- Firebase Analytics (when CocoaPods integration isn't available)
- Native iOS SDKs that don't expose Objective-C headers
- Swift Package Manager dependencies
- Any Swift-only library

### How It Works

1. **Define a Kotlin interface** in `commonMain` for your dependency
2. **Add a factory method** in `SwiftLibDependencyFactory.kt` (in `iosMain`)
3. **Implement in Swift** in `SwiftLibDependencyFactoryImpl.swift` (iOS app)
4. **Register with Koin** in `SwiftLibDependenciesModule.kt`
5. **Use anywhere** in your Kotlin code via dependency injection

### Example: Adding Firebase Analytics

#### Step 1: Create Kotlin Interface

In `common/src/commonMain/kotlin`:

```kotlin
interface Analytics {
    fun logEvent(event: String, params: Map<String, Any>?)
}
```

#### Step 2: Add Factory Method

In `common/src/iosMain/kotlin/.../di/SwiftLibDependencyFactory.kt`:

```kotlin
interface SwiftLibDependencyFactory {
    fun providePlatformInfo(): PlatformInfo
    fun provideAnalytics(): Analytics  // Add this
}
```

#### Step 3: Implement in Swift

In `ios/KMPTemplate/KMPTemplate/SwiftLibDependencyFactoryImpl.swift`:

```swift
import FirebaseAnalytics  // Add via Swift Package Manager

class FirebaseAnalyticsImpl: Analytics {
    func logEvent(event: String, params: [String : Any]?) {
        var eventParams: [String: Any] = [:]
        params?.forEach { key, value in eventParams[key] = "\(value)" }
        Analytics.logEvent(event, parameters: eventParams)
    }
}

// Add to factory:
func provideAnalytics() -> any Analytics {
    return FirebaseAnalyticsImpl()
}
```

#### Step 4: Register in Koin

In `common/src/iosMain/kotlin/.../di/SwiftLibDependenciesModule.kt`:

```kotlin
internal fun swiftLibDependenciesModule(factory: SwiftLibDependencyFactory): Module = module {
    single<PlatformInfo> { factory.providePlatformInfo() }
    single<Analytics> { factory.provideAnalytics() }  // Add this
}
```

#### Step 5: Use in Your Code

```kotlin
class MyViewModel : ViewModel(), KoinComponent {
    private val analytics: Analytics by inject()

    fun trackEvent() {
        analytics.logEvent("button_clicked", mapOf("screen" to "home"))
    }
}
```

### Current Example: PlatformInfo

The template includes a working example:
- **Interface**: `PlatformInfo` in `common/src/commonMain/kotlin/.../platform/`
- **iOS Implementation**: Uses `UIDevice` to get iOS version (Swift-only API)
- **Android Implementation**: Uses `Build.VERSION` for Android
- **Usage**: See `MainViewModel.kt` for example

### Benefits

✅ **Type-safe**: Kotlin interfaces ensure compile-time safety
✅ **Testable**: Easy to mock Swift dependencies in tests
✅ **Centralized**: All Swift dependencies registered in one place
✅ **Platform-agnostic**: Common code doesn't know about platform details
✅ **Flexible**: Add new dependencies without changing architecture

### Important Notes

- The factory is a **singleton** (`shared`) in Swift
- All Swift implementations must be **thread-safe**
- After adding dependencies, rebuild: `./gradlew :common:embedAndSignAppleFrameworkForXcode`

## Additional Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Koin Documentation](https://insert-koin.io/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [SwiftUI Documentation](https://developer.apple.com/documentation/swiftui)
- [SKIE Documentation](https://skie.touchlab.co/)

## License

This template is available for use in your own projects.
