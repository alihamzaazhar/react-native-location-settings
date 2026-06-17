# @alihamzaazhar/react-native-location-settings

An **Android-only** React Native TurboModule that shows a Google Maps-style in-app dialog to enable system location — powered by Google Play Services `ResolvableApiException`. No equivalent exists on iOS.

> Works with React Native New Architecture (TurboModules). Requires React Native 0.73+.

---

## How it works

On Android, when the system location (GPS) is turned off, most apps redirect the user to the Settings app. This library instead shows an in-app dialog — exactly like Google Maps — that lets the user enable location without ever leaving your app.

---

## Installation

```bash
npm install @alihamzaazhar/react-native-location-settings
```

### Android setup

1. **Add Google Play Services location dependency**

   In your project's root `android/build.gradle`, add the version to the `ext` block:

   ```gradle
   ext {
       // ... other versions
       playServicesLocationVersion = "21.3.0"
   }
   ```

   Then in `android/app/build.gradle`, add the dependency:

   ```gradle
   dependencies {
       implementation "com.google.android.gms:play-services-location:${playServicesLocationVersion}"
   }
   ```

2. **Add location permission to `AndroidManifest.xml`**

   ```xml
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   ```

3. **Autolinking**

   No manual linking needed — React Native autolinking handles the rest.

4. **Rebuild the app**

   ```bash
   npx react-native run-android
   ```

---

## Usage

```typescript
import LocationSettings from '@alihamzaazhar/react-native-location-settings';
import { PermissionsAndroid, Platform } from 'react-native';

async function requestLocation() {
  // Step 1: Show in-app dialog to enable system location (Android only)
  const systemEnabled = await LocationSettings.checkAndEnableSystemLocation();
  if (!systemEnabled) {
    console.log('User did not enable system location');
    return;
  }

  // Step 2: Request app-level location permission
  if (Platform.OS === 'android') {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
    );
    if (granted !== PermissionsAndroid.RESULTS.GRANTED) {
      console.log('Location permission denied');
      return;
    }
  }

  console.log('Location is ready to use!');
}
```

---

## API

### `checkAndEnableSystemLocation(): Promise<boolean>`

Shows a Google Maps-style in-app dialog asking the user to enable system location.

| Return value | Meaning |
|---|---|
| `true` | System location is enabled (user accepted or it was already on) |
| `false` | User dismissed the dialog or denied (or called on iOS) |

> **iOS**: This method is a no-op on iOS and always resolves `false`. Use the standard `PermissionsAndroid` / `react-native-permissions` API on iOS instead.

---

## Requirements

| Requirement | Version |
|---|---|
| React Native | 0.73+ |
| Android minSdk | 24+ |
| Google Play Services | Required on device |
| New Architecture | Supported (TurboModule) |

---

## Platform support

| Platform | Supported |
|---|---|
| Android | ✅ |
| iOS | ❌ (no equivalent API exists) |

---

## License

MIT © [Ali Hamza](https://github.com/alihamzaazhar)
