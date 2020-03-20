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
import com.kobbi.project.coronamask.model.Hospital
import com.kobbi.project.coronamask.util.DLog
import kotlin.concurrent.thread

class HospitalViewModel(application: Application) : AndroidViewModel(application) {
    val hospital: LiveData<List<Hospital>> get() = _hospital

    private val _hospital: MutableLiveData<List<Hospital>> = MutableLiveData()

    private val mContext = application.applicationContext

    private var mSortedValue = ""
    private val mHospitalList = mutableListOf<Hospital>()

    init {
        setHospitalFromLocation()
    }

    fun setHospitalFromAddress(address: Address?) {
        address?.run {
            val location = Location("Address").apply {
                latitude = address.latitude
                longitude = address.longitude
            }
            setHospital(location)
        }?: setHospitalFromLocation()
    }

    fun setHospitalFromLocation() {
        LocationController.getLocation(mContext, object : LocationCompleteListener {
            override fun onComplete(code: LocationController.ReturnCode, location: Location?) {
                DLog.v(
                    tag = "HospitalViewModel",
                    message = "getLocation.onComplete() --> code : $code, location : $location"
                )
                if (code == LocationController.ReturnCode.NO_ERROR)
                    location?.let {
                        setHospital(location)
                    }
            }
        })
    }

    fun setHospital(location: Location) {
        thread {
            mHospitalList.clear()
            val dataList = ClinicDatabase.getDatabase(mContext).clinicInfoDao().findFromCode("HOSPITAL")
            DLog.i(tag = "HospitalViewModel", message = "loadAll Data >> size : ${dataList.size}")
            dataList.forEach {
                val distance = location.distanceTo(Location("Address").apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }).div(1000)
                DLog.i(
                    tag = "HospitalViewModel",
                    message = "name : ${it.name}, distance : $distance"
                )
                if (distance < 10)
                    mHospitalList.add(
                        Hospital(
                            it.province,
                            it.city,
                            it.name,
                            it.address,
                            it.type,
                            it.tel,
                            it.latitude,
                            it.longitude,
                            distance
                        )
                    )
            }
            sortHospitalList(mSortedValue)
        }
    }

    fun sortHospitalList(value: String) {
        mSortedValue = value
        SortType.values().firstOrNull {
            it.sortName == value
        }?.let { type ->
            val sortedList = when (type) {
                SortType.DISTANCE -> {
                    mHospitalList.sortedBy(Hospital::distance)
                }
                SortType.ENABLE_HOSPITALIZATION -> {
                    mHospitalList.filter {
                        it.type.contains("입원")
                    }.sortedBy(Hospital::distance)
                }
            }
            _hospital.postValue(sortedList)
        }
    }

    internal enum class SortType(val sortName: String) {
        DISTANCE("거리"),
        ENABLE_HOSPITALIZATION("입원가능")
    }
}