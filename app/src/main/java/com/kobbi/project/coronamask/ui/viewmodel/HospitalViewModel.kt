package com.kobbi.project.coronamask.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.database.entity.SafetyHospital

class HospitalViewModel(application: Application) : AndroidViewModel(application) {
    val hospital: LiveData<List<SafetyHospital>> =
        ClinicDatabase.getDatabase(application).safetyHospitalDao().loadLive()
}