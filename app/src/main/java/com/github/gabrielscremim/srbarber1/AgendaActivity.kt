package com.github.gabrielscremim.srbarber1

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gabrielscremim.srbarber1.R
import java.util.Calendar

class AgendaActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var btnMarcarCorte: Button
    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        calendarView = findViewById(R.id.calendarView)
        btnMarcarCorte = findViewById(R.id.btnMarcarCorte)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis
        }

        btnMarcarCorte.setOnClickListener {
            // Função para marcar o corte no banco de dados
            DatabaseHelper(this).marcarCorte(selectedDate)
            Toast.makeText(this, "Corte marcado!", Toast.LENGTH_SHORT).show()
        }
    }
}
