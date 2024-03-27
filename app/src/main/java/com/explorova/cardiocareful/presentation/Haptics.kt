package com.explorova.cardiocareful.presentation

import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.explorova.cardiocareful.TAG
import com.explorova.cardiocareful.engine.AlertPattern
import java.time.LocalDateTime

class Haptics {
    //var endOfLastVibrate: DateTime
    val vibes: Vibrator
    var endOfLastVibrationSession: LocalDateTime = LocalDateTime.now()

    constructor(context: android.content.Context) {
        this.vibes = context.getSystemService(Vibrator::class.java)
    }

    fun sendVibration(pattern: AlertPattern) {
        startVibration(pattern.timings, pattern.amplitudes, pattern.repeatIndex)
    }

    fun vibrationAvailable() : Boolean {
        return LocalDateTime.now().isAfter(endOfLastVibrationSession)
    }

    fun startVibration(timings: LongArray, amplitudes: IntArray, repeat: Int = 0) {
        if (vibrationAvailable()) {
            val singleLength = timings.sum()
            Log.d(TAG, "Beginning vibration command expecting to take $singleLength ms")
            val copies : Int = if (repeat > 0) repeat else 1
            noteVibrationEndTime(singleLength * copies)
            vibes.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeat))
        } else
            Log.d(TAG, "Prior vibration command is (probably) still running")
    }

    fun noteVibrationEndTime(duration_ms: Long) {
        val nowtime = LocalDateTime.now()
        endOfLastVibrationSession = nowtime.plusNanos(1000000 * duration_ms)
        Log.d(TAG, "Setting vibration, now is $nowtime end time $endOfLastVibrationSession")
    }
}

