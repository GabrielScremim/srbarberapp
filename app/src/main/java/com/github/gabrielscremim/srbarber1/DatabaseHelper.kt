package com.github.gabrielscremim.srbarber1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "barbearia.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Barbeiros (id INTEGER PRIMARY KEY, nome TEXT, telefone TEXT)")
            db.execSQL("CREATE TABLE Clientes (id INTEGER PRIMARY KEY, nome TEXT, telefone TEXT)")
            db.execSQL("CREATE TABLE Agenda (id INTEGER PRIMARY KEY, data LONG, barbeiro TEXT, cliente TEXT)")
        }

        // Método para adicionar barbeiro
        fun addBarbeiro(nome: String, telefone: String) {
            val values = ContentValues().apply {
                put("nome", nome)
                put("telefone", telefone)
            }
            writableDatabase.insert("Barbeiros", null, values)
        }

        // Método para adicionar cliente
        fun addCliente(nome: String, telefone: String) {
            val values = ContentValues().apply {
                put("nome", nome)
                put("telefone", telefone)
            }
            writableDatabase.insert("Clientes", null, values)
        }

        // Método para recuperar lista de barbeiros
        fun getBarbeiros(): List<String> {
            val barbeiros = mutableListOf<String>()
            val cursor = readableDatabase.rawQuery("SELECT nome FROM Barbeiros", null)
            if (cursor.moveToFirst()) {
                do {
                    barbeiros.add(cursor.getString(cursor.getColumnIndexOrThrow("nome")))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return barbeiros
        }

        // Método para recuperar lista de clientes
        fun getClientes(): List<String> {
            val clientes = mutableListOf<String>()
            val cursor = readableDatabase.rawQuery("SELECT nome FROM Clientes", null)
            if (cursor.moveToFirst()) {
                do {
                    clientes.add(cursor.getString(cursor.getColumnIndexOrThrow("nome")))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return clientes
        }

        // Método para marcar corte
        fun marcarCorte(data: Long, barbeiro: String, cliente: String) {
            val values = ContentValues().apply {
                put("data", data)
                put("barbeiro", barbeiro)
                put("cliente", cliente)
            }
            writableDatabase.insert("Agenda", null, values)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS Barbeiros")
            db.execSQL("DROP TABLE IF EXISTS Clientes")
            db.execSQL("DROP TABLE IF EXISTS Agenda")
            onCreate(db)
        }
        // Método para buscar agendamentos por data
        fun getAgendamentosByDate(date: String): List<String> {
            val agendamentos = mutableListOf<String>()
            val cursor = readableDatabase.rawQuery("SELECT * FROM Agenda WHERE date(data/1000, 'unixepoch') = ?", arrayOf(date))
            if (cursor.moveToFirst()) {
                do {
                    val barbeiro = cursor.getString(cursor.getColumnIndexOrThrow("barbeiro"))
                    val cliente = cursor.getString(cursor.getColumnIndexOrThrow("cliente"))
                    agendamentos.add("Barbeiro: $barbeiro, Cliente: $cliente")
                } while (cursor.moveToNext())
            }
            cursor.close()
            return agendamentos
        }
    }