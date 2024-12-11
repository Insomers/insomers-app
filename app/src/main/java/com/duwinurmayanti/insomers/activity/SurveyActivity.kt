package com.duwinurmayanti.insomers.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.duwinurmayanti.insomers.R
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SurveyActivity : AppCompatActivity() {

    private lateinit var tflite: Interpreter
    private val answers = MutableList(6) { 0f }

    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioFemale: RadioButton
    private lateinit var radioMale: RadioButton
    private lateinit var radioGroup1: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup
    private lateinit var radioGroup4: RadioGroup
    private lateinit var radioGroup5: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        try {
            tflite = Interpreter(loadModelFile("insomnia_model.tflite"))
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memuat model", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        radioGroupGender = findViewById(R.id.radio_group_gender)
        radioFemale = findViewById(R.id.radio_female)
        radioMale = findViewById(R.id.radio_male)

        radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            answers[0] = when (checkedId) {
                R.id.radio_female -> 2f
                R.id.radio_male -> 1f
                else -> 0f
            }
        }

        radioGroup1 = findViewById(R.id.radio_group1)
        radioGroup2 = findViewById(R.id.radio_group2)
        radioGroup3 = findViewById(R.id.radio_group3)
        radioGroup4 = findViewById(R.id.radio_group4)
        radioGroup5 = findViewById(R.id.radio_group5)

        val radioGroups = listOf(radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5)
        radioGroups.forEachIndexed { index, group ->
            group.setOnCheckedChangeListener { _, checkedId ->
                val selectedScale = when (checkedId) {
                    R.id.radio_11, R.id.radio_21, R.id.radio_31, R.id.radio_41, R.id.radio_51 -> 1f
                    R.id.radio_12, R.id.radio_22, R.id.radio_32, R.id.radio_42, R.id.radio_52 -> 2f
                    R.id.radio_13, R.id.radio_23, R.id.radio_33, R.id.radio_43, R.id.radio_53 -> 3f
                    R.id.radio_14, R.id.radio_24, R.id.radio_34, R.id.radio_44, R.id.radio_54 -> 4f
                    R.id.radio_15, R.id.radio_25, R.id.radio_35, R.id.radio_45, R.id.radio_55 -> 5f
                    R.id.radio_16, R.id.radio_26, R.id.radio_36, R.id.radio_46, R.id.radio_56 -> 6f
                    R.id.radio_17, R.id.radio_27, R.id.radio_37, R.id.radio_47, R.id.radio_57 -> 7f
                    R.id.radio_18, R.id.radio_28, R.id.radio_38, R.id.radio_48, R.id.radio_58 -> 8f
                    R.id.radio_19, R.id.radio_29, R.id.radio_39, R.id.radio_49, R.id.radio_59 -> 9f
                    R.id.radio_10, R.id.radio_20, R.id.radio_30, R.id.radio_40, R.id.radio_50 -> 10f
                    else -> 0f
                }

                if (selectedScale != 0f) {
                    answers[index + 1] = selectedScale
                }
            }
        }

        val btnPredict: Button = findViewById(R.id.btn_predict)
        btnPredict.setOnClickListener {
            if (answers.any { it == 0f }) {
                Toast.makeText(this, "Pilih semua jawaban dulu!", Toast.LENGTH_SHORT).show()
            } else {
                val prediction = predict(answers.toFloatArray())
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("PREDICTION_RESULT", prediction)
                startActivity(intent)
            }
        }
    }

    private fun loadModelFile(modelPath: String): MappedByteBuffer {
        val assetFileDescriptor = assets.openFd(modelPath)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun predict(inputs: FloatArray): Float {
        val inputData = Array(1) { inputs }
        val outputData = Array(1) { FloatArray(1) }
        tflite.run(inputData, outputData)
        return outputData[0][0]
    }

    override fun onDestroy() {
        super.onDestroy()
        tflite.close()
    }
}