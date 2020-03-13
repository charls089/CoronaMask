package com.kobbi.project.coronamask.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ActivityMainBinding
import com.kobbi.project.coronamask.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).run {
            mainVm = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
            fragmentManager = supportFragmentManager
            lifecycleOwner = this@MainActivity
        }
    }
}
