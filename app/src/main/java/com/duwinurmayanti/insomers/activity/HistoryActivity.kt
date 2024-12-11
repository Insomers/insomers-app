package com.duwinurmayanti.insomers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duwinurmayanti.insomers.MainActivity
import com.duwinurmayanti.insomers.R
import com.duwinurmayanti.insomers.adapter.SleepHistoryAdapter
import com.duwinurmayanti.insomers.model.SleepHistoryItem
import java.util.Calendar

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sleepHistoryAdapter: SleepHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("SleepData", Context.MODE_PRIVATE)
        val sleepTimes = prefs.getStringSet("sleepTimes", mutableSetOf())?.toList() ?: emptyList()
        val wakeUpTimes = prefs.getStringSet("wakeUpTimes", mutableSetOf())?.toList() ?: emptyList()

        val sleepHistoryList = mutableListOf<SleepHistoryItem>()

        for (i in sleepTimes.indices) {
            val sleepTimeMillis = sleepTimes[i].toLong()
            val wakeUpTimeMillis = wakeUpTimes[i].toLong()

            val sleepTime = formatTime(sleepTimeMillis)
            val wakeUpTime = formatTime(wakeUpTimeMillis)

            val durationInMillis = wakeUpTimeMillis - sleepTimeMillis
            val sleepDuration = calculateSleepDuration(durationInMillis)

            sleepHistoryList.add(SleepHistoryItem(sleepTime, wakeUpTime, sleepDuration))
        }

        sleepHistoryAdapter = SleepHistoryAdapter(sleepHistoryList)
        recyclerView.adapter = sleepHistoryAdapter

        val btnBackToHome: Button = findViewById(R.id.btnBackHome)
        btnBackToHome.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
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
        val hours = (durationInMillis / (1000 * 60 * 60)).toInt()
        val minutes = ((durationInMillis % (1000 * 60 * 60)) / (1000 * 60)).toInt()

        return if (hours > 0) {
            String.format("%d jam %d menit", hours, minutes)
        } else {
            String.format("%d menit", minutes)
        }
    }
}

