package com.example.mymoovingpicture

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoovingpicture.adapter.Adapter
import com.example.mymoovingpicture.adapter.RouteListener
import com.example.mymoovingpicture.database.getDatabase
import com.example.mymoovingpicture.databinding.FragmentCoordListBinding
import com.example.mymoovingpicture.repozitory.Repozitory
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FragmentCoordList : Fragment(), CoroutineScope {
    private lateinit var viewModel: FragmentCoordViewModel// by activityViewModels()//одна вьюмодель на все фрагменты//одгна вьюмодель на все фрагменты
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    val coordddrepo by lazy { Repozitory(getDatabase(requireContext())) }
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCoordListBinding>(
            inflater,
            R.layout.fragment_coord_list, container, false
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val application = requireNotNull(this.activity).application
        val vmFactory = FragmentCoordViewModelFactory(application)
        viewModel = ViewModelProvider(
            this, vmFactory).get(FragmentCoordViewModel::class.java)
        val adapter = Adapter(
            RouteListener { itemelement ->
                viewModel.onClickDetail(itemelement)
            }
        )
        binding.coordRecyclerList.layoutManager = LinearLayoutManager(requireContext())
        binding.coordRecyclerList.adapter = adapter
        viewModel.routeList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })
        viewModel.choosenroute.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            /// navController.navigate(R.layout.football_detail)
             this.findNavController().navigate(R.id.action_fragmentCoordList_to_mapFragment)
            //navController.navigate(R.id.navigate_to_footballdetail)
        })
//        viewModel.numberlist.observe(viewLifecycleOwner, Observer {
//
//
//        })
        binding.fab.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_fragmentCoordList_to_enterRouteName)             //перех в ENTERROUTENAME FRAGment
        }
        binding.buttonDel.setOnClickListener {
            // withContext(Dispatchers.IO){
            launch {
                coordddrepo.deleteList()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //    checkPermission()
        Log.d("LOGППП", "ON START")
    }


}