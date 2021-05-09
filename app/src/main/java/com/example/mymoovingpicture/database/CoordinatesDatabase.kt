package com.example.mymoovingpicture.database

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymoovingpicture.domain.CoordinatesDomain
import com.example.mymoovingpicture.domain.RouteDomain

@Entity(tableName = "route")
data class RouteEntity(                        //WeatherDatabase
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recordRouteName")
    val recordRouteName: String,
    @ColumnInfo(name = "coordddList")
    val coordddList: LiveData<List<CoordinatesEntity>> //= null private убрал
)


fun List<RouteEntity>.asDomainRouteModel(): List<RouteDomain> { // функция для преобразования DatabaseVideo объектов базы данных в объекты домена
    return map {
        val routeDomain = RouteDomain(
            id = it.id,
            recordRouteName = it.recordRouteName,
            coordddList = it.coordddList
        )
        routeDomain
    }
}


@Entity(tableName = "coord")
data class CoordinatesEntity(                        //WeatherDatabase
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recordNumber")
    val recordNumber: Int,
    @ColumnInfo(name = "checktime")
    val checkTime: Long,// = System.currentTimeMillis(),
    @ColumnInfo(name = "lattitude")
    var Lattitude: Double,
    @ColumnInfo(name = "longittude")
    val Longittude: Double
)

fun List<CoordinatesEntity>.asDomainCoordinatesModel(): List<CoordinatesDomain> { // функция для преобразования DatabaseVideo объектов базы данных в объекты домена
    return map {
        CoordinatesDomain(
            id = it.id,
            recordNumber = it.recordNumber,
            time = it.checkTime,
            lattitude = it.Lattitude,
            longittude = it.Longittude
        )
    }
}