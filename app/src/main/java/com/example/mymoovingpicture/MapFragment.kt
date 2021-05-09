package com.example.mymoovingpicture

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mymoovingpicture.foreground_service.ForegroundService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_fragment.*
import kotlin.properties.Delegates

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {
    private lateinit var mMap: GoogleMap
    var latttitude by Delegates.notNull<Double>()
    var longitttude by Delegates.notNull<Double>()
    val application = requireNotNull(this.activity).application
    val vmFactory = FragmentCoordViewModelFactory(application)
    val viewModel = ViewModelProvider(
        this, vmFactory
    ).get(FragmentCoordViewModel::class.java)

    // val coordddrepo by lazy { Repozitory(getDatabase(application)) }
    private lateinit var viewModelMain: FragmentCoordViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelMain = ViewModelProvider(requireActivity()).get(FragmentCoordViewModel::class.java)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)               // нажатие на кнопку my location
        mMap.setOnMyLocationClickListener(this)                     // нажатие на синюю точку

//        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
//            override fun onMapClick(latlng: LatLng) {
//
//                mMap.clear();                                            // Очищает ранее затронутую позицию
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));// Перемещает камеру в новую позицию
//                val location = LatLng(latlng.latitude, latlng.longitude)
//                latttitude = latlng.latitude
//                longitttude = latlng.longitude
//                mMap.addMarker(
//                    MarkerOptions().position(location).title("$latttitude + ,$longitttude")
//                        .draggable(true)
//                )
//                viewModelMain.refreshDataFromRepository(latttitude, longitttude)
//            }
//        })
        viewModel.choosenroute.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (i in 0 until it.size) {
                val location = LatLng(it.coordddList[i].lattitude, it.coordddList[i].longittude)
                latttitude = it.coordddList[i].lattitude
                longitttude = it.coordddList[i].longittude
                mMap.addMarker(
                    MarkerOptions().position(location).title("$latttitude + ,$longitttude")
                        .draggable(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
            }


        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.map_fragment, container, false)
        var btnStop: Button = view.findViewById(R.id.buttonStopService)

        btnStop.setOnClickListener {
            ForegroundService.stopService(requireContext())
            this.findNavController().navigate(R.id.action_mapFragment_to_fragmentCoordList)
        }

        return view

    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }
}