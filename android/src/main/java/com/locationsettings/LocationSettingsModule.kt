package com.locationsettings

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient

@ReactModule(name = LocationSettingsModule.NAME)
class LocationSettingsModule(reactContext: ReactApplicationContext) :
  NativeLocationSettingsSpec(reactContext), ActivityEventListener {

  private var pendingPromise: Promise? = null

  init {
    reactContext.addActivityEventListener(this)
  }

  override fun invalidate() {
    reactApplicationContext.removeActivityEventListener(this)
    pendingPromise = null
    super.invalidate()
  }

  override fun getName(): String = NAME

  override fun checkAndEnableSystemLocation(promise: Promise) {
    val activity = currentActivity
    if (activity == null) {
      promise.reject("NO_ACTIVITY", "No foreground activity available")
      return
    }

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L).build()
    val settingsRequest = LocationSettingsRequest.Builder()
      .addLocationRequest(locationRequest)
      .setAlwaysShow(true)
      .build()

    val client: SettingsClient = LocationServices.getSettingsClient(activity)
    client.checkLocationSettings(settingsRequest)
      .addOnSuccessListener { promise.resolve(true) }
      .addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
          try {
            pendingPromise = promise
            exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
          } catch (sendEx: IntentSender.SendIntentException) {
            pendingPromise = null
            promise.reject("RESOLUTION_FAILED", "Could not show location dialog: ${sendEx.message}")
          }
        } else {
          promise.reject("SETTINGS_ERROR", exception.message)
        }
      }
  }

  override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode != REQUEST_CHECK_SETTINGS) return
    val promise = pendingPromise ?: return
    pendingPromise = null
    promise.resolve(resultCode == Activity.RESULT_OK)
  }

  override fun onNewIntent(intent: Intent) {}

  companion object {
    const val NAME = "LocationSettings"
    private const val REQUEST_CHECK_SETTINGS = 20001
  }
}
