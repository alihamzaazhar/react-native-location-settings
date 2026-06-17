# @alihamzaazhar/react-native-location-settings

A React Native TurboModule that shows a Google Maps-style in-app dialog to enable system location — powered by Google Play Services `ResolvableApiException`.

Instead of redirecting the user to the Settings app, this library shows the system location dialog **inside your app**, exactly like Google Maps does.

> Requires React Native 0.73+ with New Architecture (TurboModules).

---

## How it works

On Android, when system location (GPS) is off, the standard approach is to redirect the user to the device Settings — which breaks the app flow and hurts UX. This library uses Google Play Services `SettingsClient` to trigger a `ResolvableApiException`, which presents the system location enable dialog directly within your app. The promise resolves `true` if location becomes available, or `false` if the user declines. If system location is already enabled, the promise resolves immediately without showing any dialog.

---

## Installation

```bash
npm install @alihamzaazhar/react-native-location-settings
```

### Android setup

1. **Pin Google Play Services location version**

   In your root `android/build.gradle`, add to the `ext` block:

   ```gradle
   ext {
       // ... other versions
       playServicesLocationVersion = "21.3.0"
   }
   ```

2. **Add the dependency in `android/app/build.gradle`**

   ```gradle
   dependencies {
       implementation "com.google.android.gms:play-services-location:${playServicesLocationVersion}"
   }
   ```

3. **Add location permissions to `AndroidManifest.xml`**

   ```xml
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   ```

4. **Rebuild the app**

   ```bash
   npx react-native run-android
   ```

   No manual linking needed — React Native autolinking handles the rest.

---

## Usage

```typescript
import LocationSettings from '@alihamzaazhar/react-native-location-settings';
import { PermissionsAndroid } from 'react-native';

async function setupLocation() {
  // Calling this shows the in-app GPS enable dialog immediately.
  // The dialog is displayed DURING this await — no separate step needed.
  // Resolves true if the user enabled it, false if they dismissed.
  const systemEnabled = await LocationSettings.checkAndEnableSystemLocation();

  if (!systemEnabled) {
    // User dismissed the dialog — handle accordingly
    return;
  }

  // Step 2: Request app-level location permission
  const granted = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
  );

  if (granted === PermissionsAndroid.RESULTS.GRANTED) {
    // Location is fully ready — start reading GPS
  }
}
```

---

## API

### `checkAndEnableSystemLocation(): Promise<boolean>`

Shows the Google Maps-style in-app dialog to enable system location (GPS).

| Return value | Meaning |
|---|---|
| `true` | System location is on — either the user just enabled it or it was already on |
| `false` | User dismissed or tapped "No thanks" |

---

## Requirements

| Requirement | Version |
|---|---|
| React Native | 0.73+ |
| Android minSdk | 24+ |
| Google Play Services | Required on device |
| New Architecture | Supported (TurboModule) |

---

## License

MIT © [alihamzaazhar](https://github.com/alihamzaazhar)
