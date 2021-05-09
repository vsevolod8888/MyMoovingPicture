package com.example.mymoovingpicture.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoordDao {
    @Query("select * from route")
    fun getRoutes(): LiveData<List<RouteEntity>>

    @Insert//(onConflict = OnConflictStrategy.)
    suspend fun insertRoute(r: RouteEntity)
    @Query("select * from coord WHERE id = id")
    fun getRouteById(id: Int): RouteEntity// suspend

    @Query("DELETE FROM route")
    suspend fun deleteAllRoutes()

    @Query("DELETE FROM route WHERE id = :id")
    suspend fun deleteRouteById(id: Int)
    @Insert
    suspend fun insertCoordList(coordlist:List<CoordinatesEntity>)

    @Query("SELECT * FROM coord WHERE recordNumber =:routeId")         // получ. лист CoordinatesEntity по recordNumber
     suspend fun getCoordtList(routeId: Int): List<CoordinatesEntity?>

    suspend fun insertRouteWithCoordinates(route:RouteEntity){

//        var coordsss: List<CoordinatesEntity> = route.getCoordtList()
//        for (i in 0 until coordsss.size) {
//            coordsss.get(i).setRouteId(route.getId())
//        }
//       insertCoordList(coordsss)
//        insertRoute(route)
    }



    @Insert//(onConflict = OnConflictStrategy.)
    suspend fun insertCoord(c: CoordinatesEntity)          // suspend

    @Query("DELETE FROM coord")
    suspend fun clear()

    @Query("SELECT * FROM coord ORDER BY id DESC LIMIT 1")
    suspend fun getLastCoord(): CoordinatesEntity?

    @Query("SELECT * FROM coord ORDER BY id DESC")
    fun getAllCoords(): List<CoordinatesEntity>                                     //LiveData<>

    @Query("SELECT MAX(recordNumber) FROM coord ")
    suspend fun getLastRecordNumber(): Int?      //  LiveData<   >                                     // получить последний номер записи

    @Query("SELECT * FROM coord WHERE recordNumber=:recordNumberId ORDER BY checktime ")//
    fun getListByUnicalRecordNumber(recordNumberId: Int?): LiveData<List<CoordinatesEntity>>   // LiveData<Integer?>    список координат по определенному номеру записи

    @Query("SELECT DISTINCT recordNumber FROM coord ORDER BY recordNumber ")
    fun getOnlyRecordNumbersList(): LiveData<List<Int?>> // получить отдельный список всех пробежек

    //LiveData<List<Int>> = database.coorddao.getOnlyRecordNumbersList()

    @Query("SELECT COUNT (*) FROM coord")
    fun getCountNumberOfRecords(): Int                                        // Посчитать к-во записей в БД suspend
}