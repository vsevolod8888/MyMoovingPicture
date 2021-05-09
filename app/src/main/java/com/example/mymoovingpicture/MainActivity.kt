package com.example.mymoovingpicture

import android.app.FragmentTransaction
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mymoovingpicture.databinding.ActivityMainBinding
import com.example.mymoovingpicture.foreground_service.ForegroundService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainn)


    }
}