package com.explorova.cardiocareful

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.explorova.cardiocareful.engine.MonitorData
import com.explorova.cardiocareful.presentation.CardioCarefulApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthServicesRepository = (application as MainApplication).healthServicesRepository
        MonitorData( healthServicesRepository, applicationContext )

        setContent {
            CardioCarefulApp(healthServicesRepository = healthServicesRepository)
        }
    }

}
