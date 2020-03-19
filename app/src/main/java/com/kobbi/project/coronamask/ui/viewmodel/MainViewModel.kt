package com.kobbi.project.coronamask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobbi.project.coronamask.SingleLiveEvent

class MainViewModel : ViewModel() {
    val tabItems: LiveData<List<String>> get() = _tabItems
    val clickSearch: SingleLiveEvent<Any> = SingleLiveEvent()
    val clickMyPosition: SingleLiveEvent<Any> = SingleLiveEvent()

    private val _tabItems: MutableLiveData<List<String>> = MutableLiveData()

    companion object {
        private val TAB_ITEMS = listOf("마스크 검색", "선별 진료소", "안심병원")
    }

    init {
        _tabItems.postValue(TAB_ITEMS)
    }

    fun clickSearch() {
        clickSearch.call()
    }

    fun clickMyPosition() {
        clickMyPosition.call()
    }
}