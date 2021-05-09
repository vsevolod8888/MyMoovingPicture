package com.example.mymoovingpicture.repozitory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.mymoovingpicture.database.*
import com.example.mymoovingpicture.domain.CoordinatesDomain
import com.example.mymoovingpicture.domain.RouteDomain

class Repozitory(val database: CoordinatcchicDatabase) {

    val routeslist:LiveData<List<RouteDomain>> =
        Transformations.map(database.coorddao.getRoutes()) {
            it.asDomainRouteModel() //  используйте Transformations.map для преобразования списка объектов базы данных в список объектов домена
        }
    //val lastnumberOfList: Int? =
    suspend fun insertRoute(newRoute: RouteEntity){
        database.coorddao.insertRoute(newRoute)
    }
    suspend fun deleteAllRoutes(){
        database.coorddao.deleteAllRoutes()
    }
    suspend fun deleteRouteById(id:Int){
        database.coorddao.deleteRouteById(id)
    }
    suspend fun lastNumberOfList():Int?{
        return database.coorddao.getLastRecordNumber()// список координат под определённым номером
    }                                                  //  используйте Transformations.map для преобразования списка объектов базы данных в список объектов домена
    fun getCoordinatesByRecordNumber(recordNumber:Int): LiveData<List<CoordinatesDomain>> =
        Transformations.map(database.coorddao.getListByUnicalRecordNumber(recordNumber)) {
            it.asDomainCoordinatesModel()
        }
    val numbers: LiveData<List<Int?>> = database.coorddao.getOnlyRecordNumbersList()      // лист из номеров записей
 //   val amountOfRecords:Int = database.coorddao.getCountNumberOfRecords()                 // к-во записей инт


    suspend fun insertCoord(newcoord: CoordinatesEntity) {
        database.coorddao.insertCoord(newcoord)
    }
    suspend fun deleteList() {
        database.coorddao.clear()
    }

    suspend fun isEmptyy(): Boolean {
        if (database.coorddao.getCountNumberOfRecords() > 0) {
            return false
        } else {
            return true
        }
    }

}
