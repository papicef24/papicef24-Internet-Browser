# Android APK Build Guide for Internet Browser

## Overview

This guide explains how to build an Android APK for your Internet Browser with all features:
- ✅ Google Homepage
- ✅ Address Bar Navigation
- ✅ Back/Forward History
- ✅ Flash Player 11 Detection
- ✅ Wayback Machine (Browse by Year)
- ✅ Cookie Manager
- ✅ Full WebView Integration

## Prerequisites

### Required Software
1. **Java Development Kit (JDK)** 11 or higher
   ```bash
   # Check if installed
   java -version
   ```

2. **Android Studio**
   - Download: https://developer.android.com/studio
   - Includes Android SDK, NDK, and build tools

3. **Android SDK** (API Level 33 or higher)
   - Installed with Android Studio
   - Min SDK: API 21 (Android 5.0)

4. **Gradle** (included with Android Studio)

### System Requirements
- **OS**: Windows, macOS, or Linux
- **RAM**: 8 GB minimum (16 GB recommended)
- **Storage**: 20 GB free space
- **Processor**: Intel i5 or equivalent

## Setup Instructions

### Step 1: Install Android Studio

1. Download from https://developer.android.com/studio
2. Run the installer
3. Follow setup wizard
4. Accept SDK licenses:
   ```bash
   # In Android Studio terminal
   ./gradlew signingReport
   ```

### Step 2: Clone/Setup Project

```bash
# Clone the repository
git clone https://github.com/papicef24/papicef24-Internet-Browser.git
cd papicef24-Internet-Browser

# Navigate to Android folder
cd android
```

### Step 3: Create Signing Key (Keystore)

Required for release APK:

```bash
# Generate keystore
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias papicef24-key
```

**Prompts:**
```
Enter keystore password: [your_password]
Re-enter new password: [your_password]
First and last name: papicef24
Organizational unit: Development
Organization: papicef24
City or Locality: Your City
State or Province: Your State
Two-letter country code: US
```

### Step 4: Set Environment Variables

**On Linux/macOS:**
```bash
export KEYSTORE_PASSWORD="your_password"
export KEY_ALIAS="papicef24-key"
export KEY_PASSWORD="your_password"
```

**On Windows (PowerShell):**
```powershell
$env:KEYSTORE_PASSWORD="your_password"
$env:KEY_ALIAS="papicef24-key"
$env:KEY_PASSWORD="your_password"
```

## Building the APK

### Option 1: Using Android Studio (Recommended)

1. Open Android Studio
2. File → Open → Select `android` folder
3. Wait for Gradle sync
4. Build → Generate Signed Bundle/APK
5. Select APK
6. Choose `release` variant
7. Click Create
8. APK saved to: `android/app/release/app-release.apk`

### Option 2: Using Command Line

```bash
# Build release APK
./gradlew assembleRelease

# Build debug APK (faster, for testing)
./gradlew assembleDebug
```

**Output locations:**
- Release: `app/build/outputs/apk/release/app-release.apk`
- Debug: `app/build/outputs/apk/debug/app-debug.apk`

## Installation

### Method 1: ADB (Android Debug Bridge)

```bash
# Install on connected device
adb install -r app/build/outputs/apk/release/app-release.apk

# Uninstall if needed
adb uninstall com.papicef24.internetbrowser
```

### Method 2: Manual Installation

1. Transfer APK to Android device
2. Enable "Unknown Sources" in Settings
3. Tap APK file to install
4. Grant permissions

### Method 3: Android Studio Emulator

1. Open Android Studio
2. Tools → AVD Manager
3. Create Virtual Device (Pixel 4, API 33)
4. Run → Select device
5. App deploys automatically

## Project Structure

```
android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/
│   │       │   └── com/papicef24/internetbrowser/
│   │       │       └── MainActivity.java
│   │       └── res/
│   │           ├── layout/
│   │           │   └── activity_main.xml
│   │           └── values/
│   │               ├── strings.xml
│   │               ├── colors.xml
│   │               └── styles.xml
│   └── build.gradle
├── build.gradle
└── .gitignore
```

## Features Implemented

### Core Browser Features
- ✅ WebView with JavaScript support
- ✅ DOM Storage enabled
- ✅ Cookie management
- ✅ History navigation (back/forward)
- ✅ Pull-to-refresh
- ✅ Progress indicator

### Networking
- ✅ HTTP/HTTPS support
- ✅ Mixed content handling
- ✅ Cache management
- ✅ Network error handling

### User Interface
- ✅ Material Design
- ✅ Address bar with autocomplete
- ✅ Navigation buttons
- ✅ Status bar
- ✅ Year dropdown (Wayback Machine)
- ✅ Responsive layout

### Permissions
- ✅ Internet access
- ✅ Network state checking
- ✅ Location (if needed)
- ✅ Storage access
- ✅ Camera (for web features)

## Customization

### Change App Icon

1. Prepare icon image (512x512 PNG)
2. In Android Studio:
   - Right-click `res` → Image Asset Studio
   - Select icon image
   - Click Create
   - Approves are auto-resized for all densities

### Change App Name

Edit `android/app/src/main/res/values/strings.xml`:

```xml
<string name="app_name">Your App Name</string>
```

### Change Colors

Edit `android/app/src/main/res/values/colors.xml`:

```xml
<color name="colorPrimary">#YOUR_COLOR</color>
```

### Change Minimum SDK

Edit `android/app/build.gradle`:

```gradle
minSdkVersion 21  // Change to desired version
```

## Troubleshooting

### Build Fails with "SDK not found"

```bash
# Install required SDK
/path/to/android-sdk/tools/bin/sdkmanager "platforms;android-33"
```

### Keystore Errors

```bash
# List keystore contents
keytool -list -v -keystore keystore.jks

# If keystore is lost, create new one (APK won't be updateable)
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias papicef24-key
```

### WebView Issues

- Ensure device has Chrome/WebView installed
- Enable JavaScript in `MainActivity.java`
- Check app permissions in device settings

### Slow Build Times

```bash
# Enable build cache
export ORG_GRADLE_PROJECT_android_enableBuildCache=true

# Use parallel builds
echo "org.gradle.parallel=true" >> gradle.properties
echo "org.gradle.workers.max=4" >> gradle.properties
```

## Testing

### On Emulator

```bash
# Launch emulator
emulator -avd Pixel_4_API_33

# Install and run
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.papicef24.internetbrowser/.MainActivity
```

### On Physical Device

1. Enable Developer Mode (tap Build Number 7 times)
2. Enable USB Debugging
3. Connect device via USB
4. Run: `adb devices`
5. Deploy APK

## Release Checklist

Before publishing:

- [ ] Test on multiple devices
- [ ] Test on API 21 (min) and API 33 (latest)
- [ ] Check for crashes
- [ ] Verify all features work
- [ ] Test on both portrait and landscape
- [ ] Verify network requests work
- [ ] Check battery usage
- [ ] Test with airplane mode
- [ ] Verify permissions are necessary

## Distribution

### Google Play Store

1. Create Google Play account: https://play.google.com/apps/publish
2. Create app listing
3. Upload signed APK
4. Fill app details
5. Submit for review
6. Typically approved in 24-48 hours

### Direct Distribution

1. Host APK on website
2. Enable "Unknown Sources" on user devices
3. Direct download link
4. Users sideload APK

## Performance Tips

1. **Enable ProGuard/R8 minification** (already in build.gradle)
2. **Use WebView hardware acceleration** (default on modern devices)
3. **Implement lazy loading** for images
4. **Cache network responses**
5. **Use image compression**

## Security Considerations

✅ **Implemented:**
- HTTPS enforcement
- WebView sandbox
- Permission validation
- Code obfuscation (ProGuard)

⚠️ **Additional Recommendations:**
- Use certificate pinning for API calls
- Implement input validation
- Don't store sensitive data in SharedPreferences
- Use encrypted storage for sensitive info

## Additional Resources

- **Android Docs**: https://developer.android.com/docs
- **WebView Guide**: https://developer.android.com/guide/webapps/webview
- **Material Design**: https://material.io/design
- **Gradle Docs**: https://gradle.org/releases/

## Support

- **Issues**: https://github.com/papicef24/papicef24-Internet-Browser/issues
- **GitHub Discussions**: https://github.com/papicef24/papicef24-Internet-Browser/discussions
- **Android Developers**: https://stackoverflow.com/questions/tagged/android

---

**Happy building! 🚀**
