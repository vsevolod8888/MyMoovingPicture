package com.example.mymoovingpicture.foreground_service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.example.mymoovingpicture.MainActivity
import com.example.mymoovingpicture.R
import com.example.mymoovingpicture.database.CoordinatesEntity
import com.example.mymoovingpicture.database.RouteEntity
import com.example.mymoovingpicture.database.getDatabase
import com.example.mymoovingpicture.repozitory.Repozitory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlin.coroutines.CoroutineContext

//class ForegroundService() : LifecycleService(), CoroutineScope {
class ForegroundService() : LifecycleService(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
    var job: Job? = null
    private val CHANNEL_ID = "ForegroundService Kotlin"
    val coordddrepo by lazy { Repozitory(getDatabase(application)) } // by lazy переменная созда'тся в момент первого обращения
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            applicationContext
        )
    }
    var numbOfRecord: Int? = null
    lateinit var nameOfRoute: String


    companion object {
        var timeRepeat: Long? = null
        fun startService(
            context: Context,
            message: String
        ) {        // положил в этом методе в enterRouteFragment
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)


        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        nameOfRoute =
            intent?.getStringExtra("inputExtra").toString()                 /// тут я его достал
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification: Notification =
            getMyActivityNotification("Привет") // сразу должна біть нотификация, без ожидания

        startForeground(1, notification)
        startTimer()
//        coordddrepo.foolballlist.observe(this, Observer {
//            if (it.isEmpty()) {
//                return@Observer
//            }
//            val notText: String = it.last().lattitude.toString()
//
//
//            val notification: Notification = getMyActivityNotification(notText);
//
//            val mNotificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
//            mNotificationManager.notify(1, notification)
//
//            stopForeground(false)                                             // это для чего метод ?
//        })
    }

    fun getMyActivityNotification(s: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(s)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .build()
        return notification
    }

    fun startTimer() {
        timeRepeat?.let { setupTimer(it) }
    }

    fun setupTimer(m: Long) {
        job?.cancel()
        job = launch {
            val tickerChannel = ticker(delayMillis = m, initialDelayMillis = 5000)
            Log.d("LOG", "Запущено RecurentWork")
            // get last number
            // numbOfRecord// сюда и имя вставить
         //   var numbOfRecord: Int? = null
            if (coordddrepo.isEmptyy() == true) {            //isEmpty() не подсвечивается
                numbOfRecord = 0
            } else {
                numbOfRecord = coordddrepo.lastNumberOfList()                  // правильно ли я сделал???????????????????????????????????

                numbOfRecord = numbOfRecord?.plus(1)
//                val coordinatesListByNumber: List<CoordinatesEntity> =                      // получ. список координат по опр.маршруту
//                    coordddrepo.database.coorddao.getListByUnicalRecordNumber(numbOfRecord)
//                saveNewRoute(nameOfRoute, coordinatesListByNumber)          // suspend
                //          }

                for (event in tickerChannel) {
                    try {
                        Log.d("LOG", "Вкл. минута")
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.ACCESS_FINE_LOCATION                                            // грубая локация
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION                                          // точная локация
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return@launch
                        }
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                val lattt: Double? = location?.latitude
                                val loggg: Double? = location?.longitude
                                Log.d("LOG", "Вкл. successListener")
                                launch {
                                    if (numbOfRecord != null) {
                                        saveCurrentCoordinates(                                        // сохр.координату
                                            lattt!!,
                                            loggg!!,
                                            numbOfRecord!!
                                        )
                                    }                  //, numbOfRecord!!
                                }
                            }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    suspend fun saveNewRoute(
        name: String,
        coordinatesList: LiveData<List<CoordinatesEntity>>
    ) {                         // добавляем numOfRecord
        val newRoute =
            RouteEntity(id = 0, recordRouteName = name, coordddList = coordinatesList)
        coordddrepo.insertRoute(newRoute)// withContext(Dispatchers.IO){  // все что внутри withContext(Dispatchers.IO)  в другом потоке
    }

    suspend fun saveCurrentCoordinates(
        lat: Double,
        lon: Double,
        n: Int
    ) {                         // добавляем numOfRecord

        val newcoord = CoordinatesEntity(
            id = 0,
            recordNumber = n,                                                              //recordRouteName = nameOfRoute,
            checkTime = System.currentTimeMillis(),
            Lattitude = lat,
            Longittude = lon
        )

        /// withContext(Dispatchers.IO){       // все что внутри withContext(Dispatchers.IO)  в другом потоке
        coordddrepo.insertCoord(newcoord)
        // }

    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        launch {


        val coordinatesListByNumber: LiveData<List<CoordinatesEntity>> =                      // получ. список координат по опр.маршруту
            coordddrepo.database.coorddao.getListByUnicalRecordNumber(numbOfRecord)
        saveNewRoute(nameOfRoute, coordinatesListByNumber)          // suspend
        }
        cancel()
    }
}
