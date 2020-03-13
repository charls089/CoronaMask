package com.kobbi.project.coronamask

import android.app.Application
import com.kobbi.project.coronamask.database.ClinicDatabase

class CoronaMaskApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ClinicDatabase.initializeDB(applicationContext)
    }
}