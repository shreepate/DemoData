package com.example.demodata

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demodata.model.GoogleDirection
import com.example.demodata.viweModel.GoogleDirectionModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.io.Serializable

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener,
    GoogleMap.OnCameraIdleListener {


    override fun onCameraMoveStarted(p0: Int) {
        when (p0) {
            GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {

            }
            GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION -> {
                // "The user tapped something on the map."

            }
            GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION -> {
                //  "The app moved the camera."

            }
        }
    }

    override fun onCameraMove() {
    }

    override fun onCameraMoveCanceled() {
    }

    override fun onCameraIdle() {
//        try {
//            central_marker.visibility = View.VISIBLE
//        } catch (e: Exception) {
//        }
//        val makeMarker = mMap.cameraPosition.target
//        if (first && type.equals("")) {
//            Log.e("set", "current4")
//        setAddressData(makeMarker.latitude, makeMarker.longitude)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = false
        mMap?.uiSettings?.isCompassEnabled = false
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.uiSettings?.isMapToolbarEnabled = true
        mMap?.uiSettings?.isRotateGesturesEnabled = true

        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveCanceledListener(this)


    }

    private lateinit var mMap: GoogleMap
    private var mapFragment: MapView? = null
    var lat = ""
    var lng = ""
    var name = ""
    private var locationProvider: LocationProvider? = null
    var googleDirectionModel: GoogleDirectionModel? = null
    var points = ArrayList<LatLng>()
    private var blackPolyLine: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        mapFragment = findViewById(R.id.map) as MapView
        mapFragment?.onCreate(savedInstanceState)
        mapFragment?.onResume() // needed to get the map to display immediately
        MapsInitializer.initialize(applicationContext)
        mapFragment?.getMapAsync(this)

        if (intent != null && intent.hasExtra("lat"))
            lat = intent.getStringExtra("lat")
        if (intent != null && intent.hasExtra("lng"))
            lng = intent.getStringExtra("lng")
        if (intent != null && intent.hasExtra("name"))
            name = intent.getStringExtra("name")

        googleDirectionModel = ViewModelProviders.of(this).get(GoogleDirectionModel::class.java)

        connectLocation()

    }

    private fun connectLocation() {
        locationProvider = LocationProvider(
            this,
            LocationProvider.HIGH_ACCURACY,
            object : LocationProvider.CurrentLocationCallback {
                override fun handleNewLocation(location: Location) {
                    if (location != null) {
                        drawDirectionRoute(
                            LatLng(location.latitude, location.longitude),
                            LatLng(lat.toDouble(), lng.toDouble())
                        )
//                        setAddressData(location.latitude, location.longitude)
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )


                        locationProvider!!.disconnect()
                    }

                }

            })

        locationProvider!!.connect()
    }

    fun drawDirectionRoute(pickuplatlong: LatLng, dropDestination: LatLng) {
        googleDirectionModel?.getRoute(this!!, pickuplatlong, dropDestination)
            ?.observe(this@MapActivity,
                Observer<GoogleDirection> { googleDirectionResponse: GoogleDirection? ->
                    run {

                        drawRoutePolyLine(googleDirectionResponse!!, pickuplatlong, dropDestination)
                    }
                })


    }

    private fun drawRoutePolyLine(
        directionRouteResponse: GoogleDirection,
        pickuplatlong: LatLng,
        dropDestination: LatLng
    ) {
        if (mMap != null) {

            mMap.clear()

            points = ArrayList<LatLng>()
            points =
                decodePoly(directionRouteResponse.routes!![0]?.overviewPolyline?.points.toString())

            setPickupMarker(pickuplatlong.latitude.toDouble(), pickuplatlong.longitude.toDouble())
            setDropMarker(dropDestination.latitude.toDouble(), dropDestination.longitude.toDouble())

            blackPolyLine = mMap.addPolyline(getPolyLineOption(Color.RED, points))
            moveToBounds(blackPolyLine!!)
        }

    }

    fun setPickupMarker(lats: Double, longis: Double) {
        var mfromMarker = mMap.addMarker(
            MarkerOptions().position(LatLng(lats, longis))
                .title(name)
        )
        mfromMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_green))
    }

    fun setDropMarker(lats: Double, longis: Double) {

        var mDestinationMarker = mMap.addMarker(
            MarkerOptions().position(LatLng(lats, longis))
        )

        mDestinationMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_red))
    }

    fun getPolyLineOption(
        roadColor: Int,
        points: ArrayList<LatLng>,
        width: Float = 12f
    ): PolylineOptions {
        var blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(width)
        blackPolylineOptions.color(roadColor)
        blackPolylineOptions.startCap(SquareCap())
        blackPolylineOptions.endCap(SquareCap())
        blackPolylineOptions.jointType(JointType.ROUND)
        blackPolylineOptions.addAll(points)
        return blackPolylineOptions
    }

    fun decodePoly(encoded: String): ArrayList<LatLng> {


        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly

    }

    override fun onStop() {
        super.onStop()
        if (locationProvider != null) {
            locationProvider!!.disconnect()
        }
    }

    fun moveToBounds(p: Polyline) {

        var builder = LatLngBounds.Builder()

        for (point in p.points) {
            builder.include(point)
        }


        var bounds = builder.build()
        var padding = 150 // offset from edges of the map in pixels

        var cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cu)
    }


}