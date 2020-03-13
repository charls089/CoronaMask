package com.kobbi.project.coronamask.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.database.entity.SelectedClinic

class ClinicViewModel(application: Application) : AndroidViewModel(application) {
    val clinic: LiveData<List<SelectedClinic>> = ClinicDatabase.getDatabase(application).selectedClinicDao().loadLive()
}
