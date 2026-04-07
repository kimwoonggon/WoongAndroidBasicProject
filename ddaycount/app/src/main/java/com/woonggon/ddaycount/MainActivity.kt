package com.woonggon.ddaycount

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startButton = findViewById<Button>(R.id.startBtn)
        val endButton = findViewById<Button>(R.id.endBtn)

        var startCal: Calendar? = null
        var endCal: Calendar? = null

        startButton.setOnClickListener {
            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    startCal = GregorianCalendar(year, month, dayOfMonth)
                    startButton.text = "${year}. ${month + 1}. ${dayOfMonth}"
                }

            }, year, month, day)
            dlg.show()
         }

        endButton.setOnClickListener {
            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    endCal = GregorianCalendar(year, month, dayOfMonth)
                    endButton.text = "${year}. ${month + 1}. ${dayOfMonth}"

                    if (startCal != null) {
                        val diffMillis = endCal!!.timeInMillis - startCal!!.timeInMillis
                        val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)
                        findViewById<TextView>(R.id.finalDate).text = "${diffDays}일"
                    }
                }

            }, year, month, day)
            dlg.show()
        }

    }
}