package com.kobbi.project.coronamask.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kobbi.project.coronamask.CoronaMaskApplication
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).run {
            (application as? CoronaMaskApplication)?.let { app ->
                mainVm = app.mainViewModel.apply {
                    clickSearch.observe(this@MainActivity, Observer {
                        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                    })
                    clickMyPosition.observe(this@MainActivity, Observer {
                        app.searchViewModel.clearSearchAddress()
                        app.storeDetailViewModel.setStoreInfoFromLocation()
                        app.clinicViewModel.setClinicFromLocation()
                        app.hospitalViewModel.setHospitalFromLocation()
                    })
                }
            }
            fragmentManager = supportFragmentManager
            lifecycleOwner = this@MainActivity
        }
    }
}
