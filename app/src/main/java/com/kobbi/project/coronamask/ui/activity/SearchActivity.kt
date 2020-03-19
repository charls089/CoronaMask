package com.kobbi.project.coronamask.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kobbi.project.coronamask.CoronaMaskApplication
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ActivitySearchBinding

class SearchActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search).run {
            searchVm = (application as? CoronaMaskApplication)?.searchViewModel?.apply {
                clickAddress.observe(this@SearchActivity, Observer {
                    finish()
                })
            }
            lifecycleOwner = this@SearchActivity
        }
    }
}