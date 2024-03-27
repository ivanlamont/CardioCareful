package com.explorova.cardiocareful

import android.app.Application
import com.explorova.cardiocareful.data.HealthServicesRepository

const val TAG = "Cardio Careful"
const val PERMISSION = android.Manifest.permission.BODY_SENSORS

class MainApplication : Application() {
    val healthServicesRepository by lazy { HealthServicesRepository(this) }
}
