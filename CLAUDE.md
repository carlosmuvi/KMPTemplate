# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform (KMP) template project that shares business logic between Android and iOS platforms. The architecture uses:

- **Koin** for dependency injection across all platforms
- **ViewModel** from androidx.lifecycle for state management (shared across platforms)
- **SKIE** to generate Swift-friendly APIs from Kotlin code
- **Jetpack Compose** for Android UI
- **SwiftUI** for iOS UI

## Build Commands

### Android
```bash
# Build the project
./gradlew :app:build

# Run on device/emulator
./gradlew :app:installDebug

# Clean build
./gradlew clean build
```

### iOS
The iOS app must be built through Xcode:
1. Open `ios/KMPTemplate/KMPTemplate.xcodeproj` in Xcode
2. Build with ⌘B or Run with ⌘R

### Common Module (Shared Code)
```bash
# Build shared framework for iOS
./gradlew :common:embedAndSignAppleFrameworkForXcode

# Assemble Android library
./gradlew :common:assembleDebug

# Run all tests
./gradlew test
```

## Architecture

### Three-Module Structure

1. **`common/`** - Shared Kotlin Multiplatform module
   - `commonMain/` - Platform-agnostic business logic
   - `androidMain/` - Android-specific implementations
   - `iosMain/` - iOS-specific implementations
   - `jvmMain/` - JVM/Desktop specific implementations

2. **`app/`** - Android application module
   - Jetpack Compose UI
   - Depends on `:common` module

3. **`ios/KMPTemplate/`** - iOS application (Xcode project)
   - SwiftUI views
   - Links to compiled `KMPTemplateKit.framework` from `common` module

### Dependency Injection Flow

**Koin initialization:**
- Android: Initialized in `KMPTemplateApplication.kt` via `initKoin()`
- iOS: Initialized in `KMPTemplateApp.swift` via `KoinKt.doInitKoin()`

**Module structure:**
- `commonModule()` in `common/src/commonMain/kotlin/.../di/Koin.kt` includes all platform and feature modules
- `viewModelModule` in `ViewModelModule.kt` registers all ViewModels
- `platformModule()` uses expect/actual pattern for platform-specific dependencies

### ViewModel Pattern Across Platforms

**Shared ViewModel (in `commonMain`):**
```kotlin
class MyViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<MyState>(MyState.Loading)
    val state: StateFlow<MyState> = _state.asStateFlow()
}
```

**Android (Compose):**
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
}
```

**iOS (SwiftUI):**
```swift
struct MyView: View {
    @StateObject var vmStoreOwner = IOSViewModelStoreOwner()

    var body: some View {
        let viewModel: MyViewModel = vmStoreOwner.viewModel()

        Observing(viewModel.state) { state in
            // UI code
        }
    }
}
```

### iOS ViewModel Bridge

The iOS app uses a custom bridge to integrate Kotlin ViewModels with SwiftUI:

1. **`IOSViewModelStoreOwner`** (Swift) - ObservableObject that manages ViewModel lifecycle
2. **`ViewModelStoreOwnerProvider`** (Swift) - Environment key for dependency injection
3. **`koinResolveViewModel()`** (Kotlin) - Bridge function that resolves ViewModels from Koin using ObjC interop

This pattern ensures ViewModels are properly scoped and disposed in SwiftUI views.

## Key Configuration Files

### Version Catalog (`gradle/libs.versions.toml`)
All dependency versions are centralized here. When updating dependencies:
- Edit version numbers in `[versions]` section
- Run `./gradlew --refresh-dependencies`

### Framework Configuration (`common/build.gradle.kts`)
The iOS framework is configured as:
```kotlin
it.binaries.framework {
    export(libs.androidx.lifecycle.viewmodel)
    baseName = "KMPTemplateKit"  // ⚠️ Change this when renaming project
}
```

**Important:** If you rename the project, update:
1. `baseName` in `common/build.gradle.kts`
2. Framework references in Xcode project settings
3. Import statements in Swift files

### SKIE Configuration
SKIE is enabled in `common/build.gradle.kts`:
```kotlin
skie {
    features {
        enableSwiftUIObservingPreview = true  // Enables @Observing in SwiftUI
    }
}
```

This generates the `Observing` wrapper used in iOS views to observe Kotlin Flows.

## Adding New Features

### 1. Create ViewModel in `common/`
```kotlin
// common/src/commonMain/kotlin/.../viewmodel/MyViewModel.kt
class MyViewModel : ViewModel(), KoinComponent {
    // Business logic here
}
```

### 2. Register in Koin
```kotlin
// common/src/commonMain/kotlin/.../di/ViewModelModule.kt
val viewModelModule = module {
    viewModelOf(::MyViewModel)
}
```

### 3. Platform UI Implementation

**Android (in `app/`):**
Create Composable functions that use `koinViewModel()` to retrieve the ViewModel.

**iOS (in `ios/KMPTemplate/`):**
Create SwiftUI views that use `IOSViewModelStoreOwner` to retrieve the ViewModel.

## Xcode Project File Management

The Xcode project file (`project.pbxproj`) should only reference files that exist. When adding/removing Swift files:

1. Add files through Xcode File → Add Files
2. Or manually edit `project.pbxproj` ensuring all references are in sync:
   - `PBXBuildFile` section
   - `PBXFileReference` section
   - `PBXGroup` section
   - `PBXSourcesBuildPhase` section

Missing file references will cause build errors like "Build input files cannot be found".

## Common Issues

### iOS Build Fails After Clean
Run `./gradlew :common:embedAndSignAppleFrameworkForXcode` before building in Xcode. The Xcode project has a build phase that runs this automatically, but it may need manual triggering after major changes.

### ViewModel Not Found in Swift
Ensure the ViewModel is:
1. Registered in `viewModelModule`
2. Exported in the framework configuration
3. The framework has been rebuilt (`./gradlew :common:assembleXCFramework`)

### Koin Module Not Found
Check that `commonModule()` in `Koin.kt` includes all required modules via `includes()`.
