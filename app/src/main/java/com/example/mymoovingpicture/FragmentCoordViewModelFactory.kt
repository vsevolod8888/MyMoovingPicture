package com.example.mymoovingpicture

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FragmentCoordViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentCoordViewModel::class.java)) {
            return FragmentCoordViewModel( application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}