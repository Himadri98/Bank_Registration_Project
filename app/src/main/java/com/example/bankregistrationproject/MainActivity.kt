package com.example.bankregistrationproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val panEditText: EditText = findViewById(R.id.et_pan)
        val dateEditText: EditText = findViewById(R.id.et_date)
        val monthEditText: EditText = findViewById(R.id.et_month)
        val yearEditText: EditText = findViewById(R.id.et_year)
        val validateButton: Button = findViewById(R.id.nextButton)
        val noPanButton: TextView = findViewById(R.id.tv_noPAN)

        validateButton.isEnabled = false

        panEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateButton.isEnabled = areInputsValid(panEditText, dateEditText, monthEditText, yearEditText)
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val text = it.toString().uppercase(Locale.getDefault())
                    if (text != it.toString()) {
                        panEditText.setText(text)
                        panEditText.setSelection(text.length)
                    }
                }
            }
        })

        val birthDateWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateButton.isEnabled = areInputsValid(panEditText, dateEditText, monthEditText, yearEditText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        dateEditText.addTextChangedListener(birthDateWatcher)
        monthEditText.addTextChangedListener(birthDateWatcher)
        yearEditText.addTextChangedListener(birthDateWatcher)

        validateButton.setOnClickListener {
            val panNumber = panEditText.text.toString().trim()
            val day = dateEditText.text.toString().trim()
            val month = monthEditText.text.toString().trim()
            val year = yearEditText.text.toString().trim()

            if (isValidPan(panNumber) && isValidBirthDate(day, month, year)) {
                Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Invalid PAN number or Birth Date", Toast.LENGTH_SHORT).show()
            }
        }

        noPanButton.setOnClickListener {
            finish()
        }
    }

    private fun areInputsValid(panEditText: EditText, dateEditText: EditText, monthEditText: EditText, yearEditText: EditText): Boolean {
        return !panEditText.text.isNullOrEmpty() && !dateEditText.text.isNullOrEmpty() && !monthEditText.text.isNullOrEmpty() && !yearEditText.text.isNullOrEmpty()
    }

    companion object {
        private val panPattern = "^[A-Z]{5}[0-9]{5}$".toRegex()
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun isValidPan(pan: String): Boolean {
            return panPattern.matches(pan)
        }

        fun isValidBirthDate(day: String, month: String, year: String): Boolean {
            return try {
                val birthDate = "$day/$month/$year"
                val parsedDate = dateFormat.parse(birthDate)
                val today = Calendar.getInstance().time
                parsedDate?.before(today) ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}
