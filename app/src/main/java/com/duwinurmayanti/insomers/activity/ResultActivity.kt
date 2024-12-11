package com.duwinurmayanti.insomers.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.duwinurmayanti.insomers.R
import com.duwinurmayanti.insomers.fragment.HomeFragment

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val btnBackHome: Button = findViewById(R.id.btnBackHome)

        val predictionResult = intent.getFloatExtra("PREDICTION_RESULT", -1f)

        val resultTitle: TextView = findViewById(R.id.tvResultTitle)
        val resultDescription: TextView = findViewById(R.id.tvResultDescription)
        val resultTips: TextView = findViewById(R.id.tvResultTips)

        if (predictionResult != -1f) {
            if (predictionResult > 0.5f) {
                resultTitle.text = "Kemungkinan Kamu Mengalami Insomnia"
                resultDescription.text = "Dari hasil analisis, ada tanda-tanda kalau kamu mungkin mengalami insomnia. Jangan khawatir, kamu nggak sendirian, dan ada banyak cara buat memperbaiki kualitas tidurmu"
                resultTips.text = " Hal yang bisa kamu coba: Konsultasi ke dokter atau ahli untuk saran yang lebih tepat, Coba teknik relaksasi seperti meditasi atau pernapasan, Perhatikan pola tidur dan bikin rutinitas sebelum tidur yang nyaman. Ingat, mengetahui masalahnya adalah langkah awal buat mendapatkan tidur yang lebih nyenyak. Semangat ya, kami di sini buat mendukung kamu!"
            } else {
                resultTitle.text = "Tidak Ada Tanda Insomnia"
                resultDescription.text = "Kabar baik nih! Dari hasil analisis, nggak ada tanda-tanda kalau kamu mengalami insomnia. Tetap jaga kebiasaan tidur yang baik biar tidurmu tetap berkualitas."
                resultTips.text = "Tips buat tidur tetap nyenyak: Tidur dan bangun di jam yang sama setiap hari, termasuk akhir pekan, Bikin kamar tidur nyaman dan tenang, Kurangi waktu pakai gadget sebelum tidur biar pikiranmu lebih rileks, Kalau suatu saat kamu merasa kesulitan tidur, nggak ada salahnya buat konsultasi ke ahli. Tidur yang cukup itu penting banget buat kesehatan, lho!"
            }
        } else {
            Toast.makeText(this, "Gagal mendapatkan hasil prediksi", Toast.LENGTH_SHORT).show()
        }

        btnBackHome.setOnClickListener {
            val homeFragment = HomeFragment()

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, homeFragment)
            transaction.commit()

            finish()
        }
    }
}