package com.duwinurmayanti.insomers.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.duwinurmayanti.insomers.activity.MusicActivity
import com.duwinurmayanti.insomers.R
import com.duwinurmayanti.insomers.activity.SleepGuideActivity
import com.duwinurmayanti.insomers.activity.SleepMonitorActivity
import com.duwinurmayanti.insomers.activity.SurveyActivity
import com.duwinurmayanti.insomers.model.TimeResponse
import com.duwinurmayanti.insomers.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val fetchTimeRunnable = object : Runnable {
        override fun run() {
            fetchDateTime()
            handler.postDelayed(this, 60000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnRelaxing: Button = view.findViewById(R.id.btnRelaxing)
        btnRelaxing.setOnClickListener {
            val intent = Intent(requireContext(), MusicActivity::class.java)
            startActivity(intent)
        }

        val btnNote: Button = view.findViewById(R.id.btnNote)
        btnNote.setOnClickListener {
            val intent = Intent(requireContext(), SleepMonitorActivity::class.java)
            startActivity(intent)
        }

        val btnStep: Button = view.findViewById(R.id.btnStep)
        btnStep.setOnClickListener {
            val intent = Intent(requireContext(), SleepGuideActivity::class.java)
            startActivity(intent)
        }

        val btnSurvey: Button = view.findViewById(R.id.btnSurvey)
        btnSurvey.setOnClickListener {
            val intent = Intent(requireContext(), SurveyActivity::class.java)
            startActivity(intent)
        }

        tvDate = view.findViewById(R.id.tvDate)
        tvTime = view.findViewById(R.id.tvTime)

        fetchDateTime()
        handler.post(fetchTimeRunnable)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(fetchTimeRunnable)
    }

    private fun fetchDateTime() {
        RetrofitClient.timeApi.getCurrentTime().enqueue(object : Callback<TimeResponse> {
            override fun onResponse(call: Call<TimeResponse>, response: Response<TimeResponse>) {
                if (response.isSuccessful) {
                    val timeResponse = response.body()
                    if (timeResponse != null) {
                        tvDate.text = timeResponse.currentDate?.trim() ?: "Tanggal tidak tersedia"
                        tvTime.text = timeResponse.currentTime?.trim() ?: "Waktu tidak tersedia"
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Data kosong dari server.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("API_ERROR", "Empty response body")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.errorBody()?.string()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(
                        "API_ERROR",
                        "Error code: ${response.code()}, message: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<TimeResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat data: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}
