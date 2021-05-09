package com.example.mymoovingpicture.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mymoovingpicture.domain.CoordinatesDomain
import com.example.mymoovingpicture.domain.RouteDomain
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter("setHeading")
fun TextView.setHeading(item: RouteDomain?) {
    item?.let {
        text = "${item.recordRouteName} "
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTime")
fun TextView.setTime(item: RouteDomain?) {
    item?.let {

        val time: Long = item.coordddList[0].time
        val locale = Locale("ru", "RU")
        val date = Date(time * 1000L)
        val format2: DateFormat = SimpleDateFormat(("EEEE, dd MMMM"), locale)
        val daytoday: String = format2.format(date).capitalize()
        text = daytoday

    }
}


@SuppressLint("SetTextI18n")
@BindingAdapter("setLatitude")
fun TextView.setLatitude(item: CoordinatesDomain?) {
    item?.let {
        text = "${item.lattitude} "
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setLongitude")
fun TextView.setLongitude(item: CoordinatesDomain?) {
    item?.let {
        text = "${item.longittude} "
    }
}