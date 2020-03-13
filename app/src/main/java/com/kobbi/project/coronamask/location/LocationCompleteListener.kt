package com.kobbi.project.coronamask.location

import android.location.Location

interface LocationCompleteListener {
    fun onComplete(code: LocationController.ReturnCode, location: Location?)
}