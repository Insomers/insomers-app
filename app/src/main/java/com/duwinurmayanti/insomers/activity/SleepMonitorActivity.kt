package com.duwinurmayanti.insomers.activity

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.duwinurmayanti.insomers.R
import java.util.Calendar

class SleepMonitorActivity : AppCompatActivity() {

    private var sleepTimeInMillis: Long = 0
    private var wakeUpTimeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleepmonitor)

        val tvSleepTime: TextView = findViewById(R.id.tv_sleep_time)
        val tvWakeUpTime: TextView = findViewById(R.id.tv_wake_up_time)
        val btnSelectSleepTime: Button = findViewById(R.id.btn_select_sleep_time)
        val btnSelectWakeUpTime: Button = findViewById(R.id.btn_select_wake_up_time)
        val btnRecordSleep: Button = findViewById(R.id.btn_record_sleep)
        val btnViewHistory: Button = findViewById(R.id.btn_view_history)
        val tvSleepDuration: TextView = findViewById(R.id.tv_sleep_duration)

        btnSelectSleepTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    sleepTimeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                    }.timeInMillis
                    tvSleepTime.text = "Waktu Tidur: ${formatTime(sleepTimeInMillis)}"
                },
                hour, minute, true
            )
            timePickerDialog.show()
        }

        btnSelectWakeUpTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    wakeUpTimeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                    }.timeInMillis
                    tvWakeUpTime.text = "Waktu Bangun: ${formatTime(wakeUpTimeInMillis)}"
                },
                hour, minute, true
            )
            timePickerDialog.show()
        }

        btnRecordSleep.setOnClickListener {
            if (sleepTimeInMillis != 0L && wakeUpTimeInMillis != 0L) {
                val prefs = getSharedPreferences("SleepData", Context.MODE_PRIVATE)

                val sleepTimes = prefs.getStringSet("sleepTimes", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
                val wakeUpTimes = prefs.getStringSet("wakeUpTimes", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

                sleepTimes.add(sleepTimeInMillis.toString())
                wakeUpTimes.add(wakeUpTimeInMillis.toString())

                with(prefs.edit()) {
                    putStringSet("sleepTimes", sleepTimes)
                    putStringSet("wakeUpTimes", wakeUpTimes)
                    apply()
                }

                val duration = wakeUpTimeInMillis - sleepTimeInMillis
                tvSleepDuration.text = "Durasi Tidur: ${calculateSleepDuration(duration)}"
            }
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    private fun calculateSleepDuration(durationInMillis: Long): String {

        var adjustedDuration = durationInMillis
        if (durationInMillis < 0) {
            adjustedDuration += 24 * 60 * 60 * 1000
        }

        val hours = (adjustedDuration / (1000 * 60 * 60)).toInt()
        val minutes = ((adjustedDuration % (1000 * 60 * 60)) / (1000 * 60)).toInt()

        return if (hours > 0) {
            String.format("%d jam %d menit", hours, minutes)
        } else {
            String.format("%d menit", minutes)
        }
    }
}