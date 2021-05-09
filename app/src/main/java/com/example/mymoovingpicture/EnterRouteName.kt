package com.example.mymoovingpicture

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mymoovingpicture.foreground_service.ForegroundService
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class EnterRouteName : Fragment() {
    var sharedPref: SharedPreferences? = null
    val MyPREFERENCES = "MyPrefs"
    val BUTTON_TIME_REPEAT_: String = "button timerepeat"
    lateinit var enterName:EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.enter_route_name, container, false)
         enterName = rootView.findViewById(R.id.editTextRouteName)
        val btnEnterRouteName: Button = rootView.findViewById(R.id.buttonok)
        sharedPref = context?.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        val rg: RadioGroup = rootView.findViewById(R.id.radioGroupp)
        val lastButtonState: Int? = sharedPref?.getInt(BUTTON_TIME_REPEAT_, 1000)
        when (lastButtonState) {
            60000 -> {
                val radBut1: RadioButton = rootView.findViewById(R.id.radioButton1)
                radBut1.isChecked = true
            }
            600000 -> {
                val radBut2: RadioButton = rootView.findViewById(R.id.radioButton2)
                radBut2.isChecked = true
            }
            18000000 -> {
                val radBut3: RadioButton = rootView.findViewById(R.id.radioButton3)
                radBut3.isChecked = true
            }
        }
        btnEnterRouteName.setOnClickListener {
            val selectedId: Int = rg.checkedRadioButtonId
            when (selectedId) {
                R.id.radioButton1 -> {
                    ForegroundService.timeRepeat = 60000

                    with(sharedPref?.edit()) {
                        this?.putInt(BUTTON_TIME_REPEAT_, 60000)
                        this?.apply()
                    }
                }

                R.id.radioButton2 -> {
                    ForegroundService.timeRepeat = 600000

                    with(sharedPref?.edit()) {
                        this?.putInt(BUTTON_TIME_REPEAT_, 600000)
                        this?.apply()
                    }
                }
                R.id.radioButton3 -> {
                    ForegroundService.timeRepeat = 18000000
                    with(sharedPref?.edit()) {
                        this?.putInt(BUTTON_TIME_REPEAT_, 18000000)
                        this?.apply()
                    }
                }

            }
            checkPermissionAndStart()

        }

        return rootView
    }
    fun checkPermissionAndStart() {
        var permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Вы дали разрешение", Toast.LENGTH_SHORT).show()
               // val routeName: Editable? = enterName.text
                ForegroundService.startService(requireContext(), this@EnterRouteName.enterName.text.toString())
                this@EnterRouteName.findNavController().navigate(R.id.action_enterRouteName_to_mapFragment)
               // startLocationUpdates()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    requireContext(),
                    "Отказано в разрешении\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionlistener)
            .setPermissions(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()
    }
}