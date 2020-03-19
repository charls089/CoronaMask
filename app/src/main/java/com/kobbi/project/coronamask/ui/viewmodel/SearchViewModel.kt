package com.kobbi.project.coronamask.ui.viewmodel

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kobbi.project.coronamask.SingleLiveEvent
import com.kobbi.project.coronamask.location.LocationCompleteListener
import com.kobbi.project.coronamask.location.LocationController
import com.kobbi.project.coronamask.util.DLog
import java.io.IOException
import kotlin.concurrent.thread

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    val searchAddress: MutableLiveData<String> = MutableLiveData()
    val searchList: LiveData<List<Address>> get() = _searchList

    val address: LiveData<Address> get() = _address
    val clickAddress = SingleLiveEvent<Any>()
    private val _searchList: MutableLiveData<List<Address>> = MutableLiveData()
    private val _address: MutableLiveData<Address> = MutableLiveData()
    private val mContext = application.applicationContext

    fun setAddressListFromSearch() {
        searchAddress.value?.let { value ->
            thread {
                val addressList = mutableListOf<Address>()
                try {
                    addressList.addAll(
                        Geocoder(mContext).getFromLocationName(value, 5)
                    )
                } catch (e: IOException) {
                    DLog.e(
                        tag = "SearchViewModel",
                        message = "load error from input search search."
                    )
                    e.printStackTrace()
                }
                _searchList.postValue(addressList)
            }
        }
    }

    fun clearSearchAddress() {
        _searchList.postValue(listOf())
        searchAddress.postValue("")
        _address.postValue(null)
    }

    fun setSearchAddress(position: Int) {
        clickAddress.call()
        searchAddress.postValue("")
        _searchList.value?.let { addressList ->
            if (addressList.size > position)
                _address.postValue(addressList[position])
        }
    }

    fun setAddressListFromPosition() {
        LocationController.getLocation(mContext, object : LocationCompleteListener {
            override fun onComplete(code: LocationController.ReturnCode, location: Location?) {
                if (code == LocationController.ReturnCode.NO_ERROR)
                    location?.run {
                        thread {
                            val addressList = mutableListOf<Address>()
                            try {
                                addressList.addAll(
                                    Geocoder(mContext).getFromLocation(latitude, longitude, 5)
                                )
                            } catch (e: IOException) {
                                DLog.e(message = "load error from location.")
                            }
                            _searchList.postValue(addressList)
                        }
                    }
            }
        })
    }
}