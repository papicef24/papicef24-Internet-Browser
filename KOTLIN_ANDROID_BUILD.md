# Internet Browser - Kotlin Android Edition

## Overview

This is a modern Kotlin-based Android implementation of the Internet Browser with full features including Flash Player 11 support, Wayback Machine integration, and plugin architecture.

## Prerequisites

### System Requirements
- **OS**: Windows, macOS, or Linux
- **RAM**: 8 GB minimum (16 GB recommended)
- **Storage**: 15 GB free space
- **Java**: JDK 11 or higher

### Software
1. **Android Studio** (Latest version)
   - Download: https://developer.android.com/studio
   
2. **Android SDK** (API 33+)
   - Installed via Android Studio
   
3. **Kotlin Plugin** (Included with Android Studio)

## Project Structure

```
kotlin-android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/papicef24/internetbrowser/
│   │       │   ├── MainActivity.kt          # Main activity
│   │       │   ├── BrowserViewModel.kt      # MVVM ViewModel
│   │       │   └── BrowserWebViewClient.kt  # WebView handlers
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── styles.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Features

### Core Browser
✅ Google Homepage  
✅ Address Bar Navigation  
✅ Back/Forward History  
✅ Reload & Home buttons  
✅ Search integration  

### Advanced Features
✅ Flash Player 11 Detection  
✅ Wayback Machine (Browse by Year)  
✅ Cookie Management  
✅ PDF Viewing  
✅ Video Playback  

### Kotlin Features
✅ Coroutines for async operations  
✅ StateFlow for reactive programming  
✅ MVVM architecture  
✅ Extension functions  
✅ Data classes  
✅ Null safety  

### UI/UX
✅ Material Design  
✅ Pull-to-refresh  
✅ Progress indicator  
✅ Status bar  
✅ Responsive layout  
✅ Night mode support  

## Building the APK

### Method 1: Android Studio (Recommended)

1. Open Android Studio
2. File → Open → Select `kotlin-android` folder
3. Wait for Gradle sync
4. Build → Generate Signed Bundle/APK
5. Select APK
6. Choose release variant
7. Configure signing key
8. Click Create

### Method 2: Command Line

```bash
# Navigate to project
cd kotlin-android

# Build debug APK (faster)
./gradlew assembleDebug

# Build release APK (optimized)
./gradlew assembleRelease

# Output locations:
# Debug: app/build/outputs/apk/debug/app-debug.apk
# Release: app/build/outputs/apk/release/app-release.apk
```

## Creating Signing Key

```bash
# Generate keystore
keytool -genkey -v -keystore keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias papicef24-key

# Enter details when prompted
```

## Installation

### On Device via ADB

```bash
# Install release APK
adb install -r app/build/outputs/apk/release/app-release.apk

# Install debug APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Uninstall
adb uninstall com.papicef24.internetbrowser
```

### On Emulator

1. Start emulator: `emulator -avd Pixel_5_API_33`
2. Install: `adb install -r app-release.apk`
3. Launch: App appears on home screen

### Manual Sideload

1. Transfer APK to device
2. Enable "Unknown Sources" in Settings
3. Tap APK to install

## Architecture

### MVVM Pattern

```
UI (MainActivity)
    ↓
    ↔ BrowserViewModel (StateFlow)
    ↓
BrowserWebViewClient (WebView handling)
```

### Data Flow

```kotlin
// User interacts with UI
navigationButton.onClick → MainActivity.navigate()
    ↓
// ViewModel processes request
BrowserViewModel.navigate(url)
    ↓
// Update StateFlow
_urlToLoad.value = url
    ↓
// UI collects and updates
viewModel.urlToLoad.collect { url ->
    webView.loadUrl(url)
}
```

## Key Kotlin Features Used

### Coroutines
```kotlin
lifecycleScope.launch {
    viewModel.urlToLoad.collect { url ->
        webView.loadUrl(url)
    }
}
```

### StateFlow
```kotlin
private val _currentUrl = MutableStateFlow(homeUrl)
val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()
```

### Extension Functions
```kotlin
val webSettings = webView.settings.apply {
    javaScriptEnabled = true
    domStorageEnabled = true
}
```

### Data Classes
```kotlin
data class BrowserState(
    val currentUrl: String,
    val currentYear: String?,
    val isLoading: Boolean
)
```

## Customization

### Change App Name

Edit `kotlin-android/app/src/main/res/values/strings.xml`:

```xml
<string name="app_name">My Browser</string>
```

### Change Colors

Edit `kotlin-android/app/src/main/res/values/colors.xml`:

```xml
<color name="colorPrimary">#YOUR_COLOR</color>
```

### Change Home URL

Edit `BrowserViewModel.kt`:

```kotlin
private val homeUrl = "https://example.com"
```

## Troubleshooting

### Gradle Sync Failed

```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Resync
./gradlew clean build
```

### WebView Not Loading

Add to `MainActivity.kt`:

```kotlin
val webSettings = webView.settings.apply {
    javaScriptEnabled = true
    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
}
```

### Build Fails

1. Check Android SDK is installed: `sdkmanager --list`
2. Update Gradle: `./gradlew wrapper --gradle-version=8.0`
3. Clear build: `./gradlew clean && ./gradlew build`

## Dependencies

```gradle
// Kotlin & Coroutines
kotlinx-coroutines-android: 1.7.1
kotlinx-coroutines-core: 1.7.1

// AndroidX
appcompat: 1.6.1
lifecycle-viewmodel-ktx: 2.6.1
lifecycle-runtime-ktx: 2.6.1
webkit: 1.7.0
swiperefreshlayout: 1.1.0

// Material
com.google.android.material: 1.9.0

// Networking
okhttp3: 4.10.0
```

## Optimization

### ProGuard/R8 Rules

Already configured in `build.gradle`:

```gradle
minifyEnabled true
proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
```

### Build Size

- **Debug APK**: ~15-20 MB
- **Release APK**: ~5-8 MB (with ProGuard)

### Performance

✅ Coroutine-based async operations  
✅ Lazy initialization of components  
✅ Efficient view binding  
✅ Memory-efficient WebView lifecycle  

## Testing

### Unit Tests

```bash
./gradlew test
```

### Instrumented Tests

```bash
./gradlew connectedAndroidTest
```

## Distribution

### Google Play Store

1. Create developer account
2. Generate signed APK
3. Upload to Play Console
4. Fill app details
5. Submit for review

### Direct Distribution

1. Host APK on website
2. Users download and install
3. Enable "Unknown Sources" on device

## Documentation

- **Android Docs**: https://developer.android.com/docs
- **Kotlin Docs**: https://kotlinlang.org/docs
- **Coroutines**: https://kotlinlang.org/docs/coroutines-overview.html
- **Jetpack**: https://developer.android.com/jetpack

## Support

- **Issues**: https://github.com/papicef24/papicef24-Internet-Browser/issues
- **Discussions**: https://github.com/papicef24/papicef24-Internet-Browser/discussions

---

**Happy coding! 🚀**
