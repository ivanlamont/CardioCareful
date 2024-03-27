package com.explorova.cardiocareful.engine

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.explorova.cardiocareful.TAG
import com.explorova.cardiocareful.data.CardioMessage
import com.explorova.cardiocareful.data.HealthServicesRepository
import com.explorova.cardiocareful.presentation.Haptics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class MonitorData(
    healthServicesRepository: HealthServicesRepository,
    context: android.content.Context
) {

    private val notifications: MutableList<Notification> = loadNotifications()
    private val heartrate_bpm: MutableState<Double> = mutableStateOf(0.0)
    private val haptics: Haptics = Haptics(context)

    init {

        val enabled: MutableStateFlow<Boolean> = MutableStateFlow(true)

        val scope = kotlinx.coroutines.MainScope()

        scope.launch {
            enabled.collect {
                if (it) {
                    healthServicesRepository.heartRateMeasureFlow()
                        .takeWhile { enabled.value }
                        .collect { cardioDataMessage ->
                            when (cardioDataMessage) {
                                is CardioMessage.CardioData -> {
                                    heartrate_bpm.value = cardioDataMessage.data.last().value
                                    checkData(notifications, heartrate_bpm.value)
                                }
                                else -> {

                                }
                            }
                        }
                }
            }
        }
    }

    fun loadNotifications() : MutableList<Notification> {
        return Notification.getSampleData()
    }

    fun checkData(notifications: MutableList<Notification>, currentHeartRate: Double) {
        Log.d(TAG, "heart rate $currentHeartRate")
        for(item in notifications) {
            Log.d(TAG, "checking condition $item")
            if (item.checkConditions(currentHeartRate))
                haptics.sendVibration(item.pattern)
            else
                Log.d(TAG, "condition not fired")
        }
    }

}