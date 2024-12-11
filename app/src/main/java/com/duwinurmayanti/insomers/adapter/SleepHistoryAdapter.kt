package com.duwinurmayanti.insomers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duwinurmayanti.insomers.R
import com.duwinurmayanti.insomers.model.SleepHistoryItem

class SleepHistoryAdapter(private val sleepHistoryList: List<SleepHistoryItem>) : RecyclerView.Adapter<SleepHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sleep_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sleepHistory = sleepHistoryList[position]
        holder.tvSleepTime.text = "Waktu Tidur: ${sleepHistory.sleepTime}"
        holder.tvWakeUpTime.text = "Waktu Bangun: ${sleepHistory.wakeUpTime}"
        holder.tvDuration.text = "Durasi Tidur: ${sleepHistory.sleepDuration}"
    }

    override fun getItemCount(): Int {
        return sleepHistoryList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSleepTime: TextView = view.findViewById(R.id.tv_sleep_time)
        val tvWakeUpTime: TextView = view.findViewById(R.id.tv_wake_up_time)
        val tvDuration: TextView = view.findViewById(R.id.tv_duration)
    }
}