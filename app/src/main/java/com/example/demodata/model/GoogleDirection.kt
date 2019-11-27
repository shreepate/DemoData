package com.example.demodata.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoogleDirection(
    @SerializedName("geocoded_waypoints")
    var geocodedWaypoints: List<GeocodedWaypoint?>? = listOf(),
    @SerializedName("routes")
    var routes: List<Route?>? = listOf(),
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("error_message")
    var error_message: String? = ""
) : Serializable

data class Route(
    @SerializedName("bounds")
    var bounds: Bounds? = Bounds(),
    @SerializedName("copyrights")
    var copyrights: String? = "",
    @SerializedName("legs")
    var legs: List<Leg?>? = listOf(),
    @SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolyline? = OverviewPolyline(),
    @SerializedName("summary")
    var summary: String? = "",
    @SerializedName("warnings")
    var warnings: List<Any?>? = listOf(),
    @SerializedName("waypoint_order")
    var waypointOrder: List<Any?>? = listOf()
) : Serializable

data class Bounds(
    @SerializedName("northeast")
    var northeast: Northeast? = Northeast(),
    @SerializedName("southwest")
    var southwest: Southwest? = Southwest()
) : Serializable

data class Northeast(
    @SerializedName("lat")
    var lat: Double? = 0.0,
    @SerializedName("lng")
    var lng: Double? = 0.0
) : Serializable

data class Southwest(
    @SerializedName("lat")
    var lat: Double? = 0.0,
    @SerializedName("lng")
    var lng: Double? = 0.0
) : Serializable

data class OverviewPolyline(
    @SerializedName("points")
    var points: String? = ""
) : Serializable

data class Leg(
    @SerializedName("distance")
    var distance: Distance? = Distance(),
    @SerializedName("duration")
    var duration: Duration? = Duration(),
    @SerializedName("end_address")
    var endAddress: String? = "",
    @SerializedName("end_location")
    var endLocation: EndLocation? = EndLocation(),
    @SerializedName("start_address")
    var startAddress: String? = "",
    @SerializedName("start_location")
    var startLocation: StartLocation? = StartLocation(),
    @SerializedName("steps")
    var steps: List<Step?>? = listOf(),
    @SerializedName("traffic_speed_entry")
    var trafficSpeedEntry: List<Any?>? = listOf(),
    @SerializedName("via_waypoint")
    var viaWaypoint: List<Any?>? = listOf()
) : Serializable

data class Distance(
    @SerializedName("text")
    var text: String? = "",
    @SerializedName("value")
    var value: Int? = 0
) : Serializable

data class Step(
    @SerializedName("distance")
    var distance: Distance? = Distance(),
    @SerializedName("duration")
    var duration: Duration? = Duration(),
    @SerializedName("end_location")
    var endLocation: EndLocation? = EndLocation(),
    @SerializedName("html_instructions")
    var htmlInstructions: String? = "",
    @SerializedName("maneuver")
    var maneuver: String? = "",
    @SerializedName("polyline")
    var polyline: Polyline? = Polyline(),
    @SerializedName("start_location")
    var startLocation: StartLocation? = StartLocation(),
    @SerializedName("travel_mode")
    var travelMode: String? = ""
) : Serializable

data class StartLocation(
    @SerializedName("lat")
    var lat: Double? = 0.0,
    @SerializedName("lng")
    var lng: Double? = 0.0
) : Serializable

data class EndLocation(
    @SerializedName("lat")
    var lat: Double? = 0.0,
    @SerializedName("lng")
    var lng: Double? = 0.0
) : Serializable

data class Polyline(
    @SerializedName("points")
    var points: String? = ""
) : Serializable

data class Duration(
    @SerializedName("text")
    var text: String? = "",
    @SerializedName("value")
    var value: Int? = 0
) : Serializable

data class GeocodedWaypoint(
    @SerializedName("geocoder_status")
    var geocoderStatus: String? = "",
    @SerializedName("place_id")
    var placeId: String? = "",
    @SerializedName("types")
    var types: List<String?>? = listOf()
) : Serializable