package com.widi.scan.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.widi.scan.R
import kotlin.math.cos

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            showUserLocation()
        }
    }

    private fun showUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    addDropPointMarkersAroundUser(userLocation, 7, 5.0)
                }
            }
        }
    }

    private fun addDropPointMarkersAroundUser(userLocation: LatLng, numberOfDropPoints: Int, maxDistance: Double) {
        val dropPoints = generateDropPoints(userLocation, numberOfDropPoints, maxDistance)
        addDropPointMarkers(dropPoints)
    }

    private fun generateDropPoints(userLocation: LatLng, numberOfDropPoints: Int, maxDistance: Double): List<LatLng> {
        val dropPoints = mutableListOf<LatLng>()

        repeat(numberOfDropPoints) {
            val latOffset = (Math.random() - 0.5) * maxDistance / 111.0
            val lngOffset = (Math.random() - 0.5) * maxDistance / (111.0 * cos(userLocation.latitude))
            val lat = userLocation.latitude + latOffset
            val lng = userLocation.longitude + lngOffset
            dropPoints.add(LatLng(lat, lng))
        }

        return dropPoints
    }

    private fun addDropPointMarkers(dropPoints: List<LatLng>) {
        dropPoints.forEachIndexed { index, location ->
            mMap.addMarker(MarkerOptions().position(location).title("Drop Point ${index + 1}"))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showUserLocation()
        } else {
            Snackbar.make(requireView(), "Location permission denied", Snackbar.LENGTH_LONG).show()
        }
    }

}
