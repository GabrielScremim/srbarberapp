package com.github.gabrielscremim.srbarber1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AgendaActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var btnMarcarCorte: Button
    private lateinit var spinnerBarbeiro: Spinner
    private lateinit var spinnerCliente: Spinner
    private lateinit var listViewAgendamentos: ListView

    private var selectedDate: Long = System.currentTimeMillis()
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        // Inicializando os elementos da tela
        calendarView = findViewById(R.id.calendarView)
        btnMarcarCorte = findViewById(R.id.btnMarcarCorte)
        spinnerBarbeiro = findViewById(R.id.spinnerBarbeiro)
        spinnerCliente = findViewById(R.id.spinnerCliente)
        listViewAgendamentos = findViewById(R.id.listViewAgendamentos)

        // Inicializando o DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Carregar barbeiros e clientes
        loadBarbeiros()
        loadClientes()

        // Listener para o CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis

            // Carregar agendamentos para o dia selecionado
            loadAgendamentos(selectedDate)
        }

        // Listener para o botão "Marcar Corte"
        btnMarcarCorte.setOnClickListener {
            val selectedBarbeiro = spinnerBarbeiro.selectedItem as String
            val selectedCliente = spinnerCliente.selectedItem as String

            if (selectedBarbeiro.isNotEmpty() && selectedCliente.isNotEmpty()) {
                databaseHelper.marcarCorte(selectedDate, selectedBarbeiro, selectedCliente)
                Toast.makeText(this, "Corte marcado com sucesso!", Toast.LENGTH_SHORT).show()

                // Atualizar a lista de agendamentos após adicionar um novo
                loadAgendamentos(selectedDate)
            } else {
                Toast.makeText(this, "Selecione um barbeiro e um cliente", Toast.LENGTH_SHORT).show()
            }
        }

        // Carregar agendamentos para a data atual na inicialização
        loadAgendamentos(selectedDate)
    }

    private fun loadBarbeiros() {
        val barbeiros = databaseHelper.getBarbeiros()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, barbeiros)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBarbeiro.adapter = adapter
    }

    private fun loadClientes() {
        val clientes = databaseHelper.getClientes()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCliente.adapter = adapter
    }

    // Função para carregar e exibir agendamentos no ListView
    private fun loadAgendamentos(date: Long) {
        val dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date))
        val agendamentos = databaseHelper.getAgendamentosByDate(dateFormatted)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, agendamentos)
        listViewAgendamentos.adapter = adapter
    }
}
