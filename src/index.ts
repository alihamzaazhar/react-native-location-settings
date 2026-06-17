import { Platform } from 'react-native';
import NativeLocationSettings from './NativeLocationSettings';
export type { Spec } from './NativeLocationSettings';

const LocationSettings = {
  checkAndEnableSystemLocation(): Promise<boolean> {
    if (Platform.OS !== 'android') {
      return Promise.resolve(false);
    }
    return NativeLocationSettings.checkAndEnableSystemLocation();
  },
};

export default LocationSettings;
