package com.example.mymoovingpicture.domain

import androidx.lifecycle.LiveData
import com.example.mymoovingpicture.database.CoordinatesEntity

data class RouteDomain(
    val id: Int,
    val recordRouteName: String,
    val coordddList: LiveData<List<CoordinatesEntity>>
    )
data class CoordinatesDomain(
    val id:Int,
    val recordNumber: Int,
    val time:Long,
    val lattitude: Double,
    val longittude: Double
)