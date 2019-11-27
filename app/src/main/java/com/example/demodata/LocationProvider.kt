package com.example.demodata



/**
 * Created by Chandresh on 14/12/2017.
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener


class LocationProvider( val mContext: Context, locationPriority: Int, val currentLocationCallback: CurrentLocationCallback) {

    private var mLocationCallback: LocationCallback? = null
    /**
     * Provides access to the Fused Location Provider API.
     */
    private val mFusedLocationClient: FusedLocationProviderClient
    /**
     * Provides access to the Location Settings API.
     */
    private val mSettingsClient: SettingsClient
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest=LocationRequest()

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null



    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        mSettingsClient = LocationServices.getSettingsClient(mContext)
        createLocationCallback()
        createLocationRequest(locationPriority)
        buildLocationSettingsRequest()


    }

    /**
     * Location callback get last location
     */

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null)
                    currentLocationCallback.handleNewLocation(locationResult.lastLocation)

            }
        }
    }

    /**
     * Uses a [LocationSettingsRequest.Builder] to build
     * a [LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest(locationPriority: Int) {
        mLocationRequest = LocationRequest()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.interval = 5000

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.fastestInterval = 3000

        when (locationPriority) {
            0 -> mLocationRequest.priority = LocationRequest.PRIORITY_NO_POWER
            1 -> mLocationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
            2 -> mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            3 -> mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    }

    fun connect() {

        startLocationUpdates()
    }

    fun disconnect() {
        stopLocationUpdates()
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(mContext as Activity) {
                Log.i(TAG, "All location settings are satisfied.")


                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
            }
            .addOnFailureListener(mContext, OnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(mContext as Activity) { }
    }

    /**
     * chack Gps on or off
     * @param context
     * @return true if gps service on
     */
    fun checkGpsStatus(context: Context): Boolean? {


        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            true
        else

            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }


    fun openGpsDialog() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(mContext as Activity
            ) { Log.i(TAG, "All location settings are satisfied.") }
            .addOnFailureListener(mContext, OnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * Callback for Location events.
     */
    interface CurrentLocationCallback {
        fun handleNewLocation(location: Location)
    }

    companion object {

        val TAG = LocationProvider::class.java.simpleName

        val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000

        /**
         * Location Priority
         */
        val NO_POWER = 0
        val LOW_POWER = 1
        val BALANCED_POWER_ACCURACY = 2
        val HIGH_ACCURACY = 3
    }


}
