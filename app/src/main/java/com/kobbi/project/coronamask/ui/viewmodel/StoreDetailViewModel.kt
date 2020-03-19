package com.kobbi.project.coronamask.ui.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kobbi.project.coronamask.location.LocationCompleteListener
import com.kobbi.project.coronamask.location.LocationController
import com.kobbi.project.coronamask.model.RemainState
import com.kobbi.project.coronamask.model.SortType
import com.kobbi.project.coronamask.model.Store
import com.kobbi.project.coronamask.presenter.ApiRepository
import com.kobbi.project.coronamask.util.DLog
import com.kobbi.project.coronamask.util.Utils
import java.util.*
import kotlin.Comparator
import kotlin.concurrent.thread

class StoreDetailViewModel(application: Application) : AndroidViewModel(application) {
    val stores: LiveData<List<Store>> get() = _stores
    val updateTime: LiveData<Date> get() = _updateTime

    private val _stores: MutableLiveData<List<Store>> = MutableLiveData()
    private val _updateTime: MutableLiveData<Date> = MutableLiveData()

    private val mSortList: List<SortType> = SortType.values().toList()
    private val mStoreList = mutableListOf<Store>()

    private var mPage = 1

    private var mSortedValue = ""
    private var mLastLocation: Location? = null

    private val mContext = application.applicationContext

    init {
        setStoreInfoFromLocation()
    }

    fun setStoreInfoFromLocation() {
        LocationController.getLocation(mContext, object :
            LocationCompleteListener {
            override fun onComplete(code: LocationController.ReturnCode, location: Location?) {
                if (code == LocationController.ReturnCode.NO_ERROR) {
                    requestStoresDetail(location)
                }
            }
        })
    }

    fun setStoreInfoFromAddress(address: Address?) {
        address?.run {
            val location = Location("Address").apply {
                latitude = address.latitude
                longitude = address.longitude
            }
            requestStoresDetail(location)
        } ?: setStoreInfoFromLocation()
    }

    fun sortStoreList(value: String) {
        thread {
            mSortedValue = value
            mSortList.firstOrNull {
                it.sortName == value
            }?.let { type ->
                val sortedValue = mStoreList.sortedWith(Comparator { p0, p1 ->
                    when (type) {
                        SortType.DISTANCE -> {
                            if (p0.distance < p1.distance) -1 else 1
                        }
                        SortType.STOCK_AT -> {
                            val p0Time = p0.stockAt?.time
                            val p1Time = p1.stockAt?.time
                            if (p0Time != null && p1Time != null)
                                when {
                                    p0Time < p1Time -> 1
                                    p0Time == p1Time -> if (p0.distance < p1.distance) -1 else 1
                                    else -> -1
                                }
                            else
                                -1
                        }
                        SortType.REMAIN_STAT -> {
                            val p0remainStat = p0.remainStat.level.last
                            val p1remainStat = p1.remainStat.level.last
                            when {
                                p0remainStat < p1remainStat -> 1
                                p0remainStat == p1remainStat -> if (p0.distance < p1.distance) -1 else 1
                                else -> -1
                            }
                        }
                    }
                })
                _stores.postValue(sortedValue)
            }
        }
    }

    fun refreshData() {
        requestStoresDetail(mLastLocation)
    }

    fun selectItem(position: Int) {
        _stores.value?.let { stores ->
            if (stores.size > position)
                stores[position].let { store ->
                    //TODO 약국 클릭시 보여줄 것 확인하기
                }
        }
    }

    private fun requestStoresDetail(location: Location?) {
        location?.run {
            mLastLocation = location
            ApiRepository.requestStoreDetails(latitude, longitude, mPage * 1000)
                .subscribe { response ->
                    DLog.d(message = "response : $response")
                    mStoreList.clear()
                    var updateTime: Date? = null
                    response?.stores?.forEach { details ->
                        val distance = location.distanceTo(Location("store").apply {
                            latitude = details.lat
                            longitude = details.lng
                        }).toInt()
                        mStoreList.add(
                            Store(
                                details.address,
                                details.code,
                                details.lat,
                                details.lng,
                                details.name,
                                details.type,
                                Utils.convertStringToDate(date = details.stockAt),
                                RemainState.getState(details.remainStat),
                                distance
                            )
                        )
                        details.createdAt?.let { createdAt ->
                            Utils.convertStringToDate(date = createdAt)?.let { date ->
                                if (updateTime == null || updateTime!!.time - date.time < 0)
                                    updateTime = date
                            }

                        }
                    }
                    _updateTime.postValue(updateTime)
                    sortStoreList(mSortedValue)
                }
        }
    }
}
