package com.example.mymoovingpicture

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.mymoovingpicture.database.CoordDao
import com.example.mymoovingpicture.database.CoordinatesEntity
import com.example.mymoovingpicture.database.getDatabase
import com.example.mymoovingpicture.domain.RouteDomain
import com.example.mymoovingpicture.repozitory.Repozitory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//data class Location(val lat: Double, val lon: Double)

class FragmentCoordViewModel(application: Application) : AndroidViewModel(application) {
    // var coordinatess: Location? = null
    // var db = dat
    var coordddrepo: Repozitory = Repozitory(getDatabase(application))

   // val coordList = coordddrepo.foolballlist
    val routeList = coordddrepo.routeslist
    val numberlist = coordddrepo.numbers                                             // лист из номеров записей
    //val countHowMuchRecords = coordddrepo.amountOfRecords                            // сколько записей у нас есть инт

    private val _choosenroute = MutableLiveData<RouteDomain>()
    val choosenroute: LiveData<RouteDomain>
        get() = _choosenroute

    fun onClickDetail(choosenf: RouteDomain) {
        _choosenroute.value = choosenf
    }
}