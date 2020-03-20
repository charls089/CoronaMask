package com.kobbi.project.coronamask.ui.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.location.LocationCompleteListener
import com.kobbi.project.coronamask.location.LocationController
import com.kobbi.project.coronamask.model.Clinic
import com.kobbi.project.coronamask.util.DLog
import kotlin.concurrent.thread

class ClinicViewModel(application: Application) : AndroidViewModel(application) {
    val clinic: LiveData<List<Clinic>> get() = _clinic

    private val _clinic: MutableLiveData<List<Clinic>> = MutableLiveData()

    private val mContext = application.applicationContext

    private var mSortedValue = ""
    private val mClinicList = mutableListOf<Clinic>()

    init {
        setClinicFromLocation()
    }

    fun setClinicFromAddress(address: Address?) {
        address?.run {
            val location = Location("Address").apply {
                latitude = address.latitude
                longitude = address.longitude
            }
            setClinic(location)
        } ?: setClinicFromLocation()
    }

    fun setClinicFromLocation() {
        LocationController.getLocation(mContext, object : LocationCompleteListener {
            override fun onComplete(code: LocationController.ReturnCode, location: Location?) {
                DLog.v(
                    tag = "ClinicViewModel",
                    message = "getLocation.onComplete() --> code : $code, location : $location"
                )
                if (code == LocationController.ReturnCode.NO_ERROR)
                    location?.let {
                        setClinic(location)
                    }
            }
        })
    }

    fun setClinic(location: Location) {
        thread {
            mClinicList.clear()
            val dataList =
                ClinicDatabase.getDatabase(mContext).clinicInfoDao().findFromCode("CLINIC")
            DLog.i(tag = "ClinicViewModel", message = "loadAll Data >> size : ${dataList.size}")
            dataList.forEach {
                val distance = location.distanceTo(Location("Address").apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }).div(1000)
                DLog.i(
                    tag = "ClinicViewModel",
                    message = "name : ${it.name}, distance : $distance"
                )
                if (distance < 10)
                    mClinicList.add(
                        Clinic(
                            it.province,
                            it.city,
                            it.name,
                            it.address,
                            it.tel,
                            it.type == "O",
                            it.latitude,
                            it.longitude,
                            distance
                        )
                    )
            }
            sortClinicList(mSortedValue)
        }
    }

    fun sortClinicList(value: String) {
        mSortedValue = value
        SortType.values().firstOrNull {
            it.sortName == value
        }?.let { type ->
            val sortedList = when (type) {
                SortType.DISTANCE -> {
                    mClinicList.sortedBy(Clinic::distance)
                }
                SortType.ENABLE_COLLECT -> {
                    mClinicList.filter {
                        it.enableCollect
                    }.sortedBy(Clinic::distance)
                }
            }
            _clinic.postValue(sortedList)
        }
    }

    internal enum class SortType(val sortName: String) {
        DISTANCE("거리"),
        ENABLE_COLLECT("검체체취 가능")
    }
}
