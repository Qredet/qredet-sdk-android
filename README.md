# Qredet SDK Android

[![](https://jitpack.io/v/Qredet/qredet-sdk-android.svg)](https://jitpack.io/#Qredet/qredet-sdk-android)

Qredet is a contactless payment SDK for Android that enables devices to:
- Act as payment terminals (Phone-to-Phone payments)
- Emulate payment cards using Host Card Emulation (HCE)
- Process contactless transactions between devices

## Features

- ✅ **Phone-to-Phone payments** via HCE
- ✅ **Event-driven architecture**
- <!-- ✅ **Backend API integration** - commented out for now, may be added later -->
- ✅ **Error handling** for various scenarios
- ✅ **Published as reusable SDK**

## Installation

Add the JitPack repository to your project's `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Qredet:qredet-sdk-android:1.1.5'
}
```

## Usage

### Basic Setup

```kotlin
val qredet = Qredet(
    apiKey = "your-api-key",
    onTransactionInitiated = { data -> 
        // Handle transaction initiation
    },
    onTransactionCompleted = { data -> 
        // Handle transaction completion
    }
)
```

### Start a Transaction (Merchant Side)

```kotlin
qredet.startTransaction(
    context = this,
    id = "transaction-id",
    amount = 100.0,
    extra = mapOf("merchantName" to "Demo Merchant")
)
```

### Complete a Transaction (Customer Side)

```kotlin
qredet.completeTransaction(this)
```

## Permissions

Add these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.VIBRATE" />

<uses-feature android:name="android.hardware.nfc" android:required="false" />
<uses-feature android:name="android.hardware.nfc.hce" android:required="false" />
```

## Requirements

- Android API level 21+
- NFC-enabled device
<!-- - Internet connection for backend integration - commented out for now -->

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

For support, email support@qredet.co or create an issue on GitHub.
