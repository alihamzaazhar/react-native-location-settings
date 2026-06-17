package com.locationsettings

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class LocationSettingsPackage : BaseReactPackage() {

  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    return if (name == LocationSettingsModule.NAME) LocationSettingsModule(reactContext) else null
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      mapOf(
        LocationSettingsModule.NAME to ReactModuleInfo(
          LocationSettingsModule.NAME,
          LocationSettingsModule.NAME,
          false,
          false,
          false,
          true,
        )
      )
    }
  }
}
