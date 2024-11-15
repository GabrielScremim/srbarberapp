package com.github.gabrielscremim.srbarber1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "barbearia.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Barbeiros (id INTEGER PRIMARY KEY, nome TEXT, telefone TEXT)")
        db.execSQL("CREATE TABLE Clientes (id INTEGER PRIMARY KEY, nome TEXT, telefone TEXT)")
        db.execSQL("CREATE TABLE Agenda (id INTEGER PRIMARY KEY, data LONG)")
    }

    fun addBarbeiro(nome: String, telefone: String) {
        val values = ContentValues().apply {
            put("nome", nome)
            put("telefone", telefone)
        }
        writableDatabase.insert("Barbeiros", null, values)
    }

    fun addCliente(nome: String, telefone: String) {
        val values = ContentValues().apply {
            put("nome", nome)
            put("telefone", telefone)
        }
        writableDatabase.insert("Clientes", null, values)
    }

    fun marcarCorte(data: Long) {
        val values = ContentValues().apply {
            put("data", data)
        }
        writableDatabase.insert("Agenda", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Barbeiros")
        db.execSQL("DROP TABLE IF EXISTS Clientes")
        db.execSQL("DROP TABLE IF EXISTS Agenda")
        onCreate(db)
    }
}
