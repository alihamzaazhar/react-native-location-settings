import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  /**
   * Android only — shows a Google Maps-style in-app dialog to enable system
   * location via ResolvableApiException (Google Play Services).
   * No equivalent exists on iOS.
   *
   * Resolves `true` if the user enabled location, `false` if dismissed/denied.
   */
  checkAndEnableSystemLocation(): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('LocationSettings');
