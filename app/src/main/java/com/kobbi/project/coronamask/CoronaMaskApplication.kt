package com.kobbi.project.coronamask

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.kobbi.project.coronamask.ui.viewmodel.*

class CoronaMaskApplication : Application() {
    val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this).create(SearchViewModel::class.java)
    }

    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }

    val storeDetailViewModel: StoreDetailViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this).create(StoreDetailViewModel::class.java)
    }

    val clinicViewModel: ClinicViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this).create(ClinicViewModel::class.java)
    }

    val hospitalViewModel: HospitalViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this).create(HospitalViewModel::class.java)
    }
}