package com.github.gabrielscremim.srbarber1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gabrielscremim.srbarber1.R

class CadastroBarbeiroActivity : AppCompatActivity() {

    private lateinit var edtNomeBarbeiro: EditText
    private lateinit var edtTelefoneBarbeiro: EditText
    private lateinit var btnSalvarBarbeiro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_barbeiro)

        edtNomeBarbeiro = findViewById(R.id.edtNomeBarbeiro)
        edtTelefoneBarbeiro = findViewById(R.id.edtTelefoneBarbeiro)
        btnSalvarBarbeiro = findViewById(R.id.btnSalvarBarbeiro)

        btnSalvarBarbeiro.setOnClickListener {
            val nome = edtNomeBarbeiro.text.toString()
            val telefone = edtTelefoneBarbeiro.text.toString()

            if (nome.isNotEmpty() && telefone.isNotEmpty()) {
                // Salvando no banco de dados
                DatabaseHelper(this).addBarbeiro(nome, telefone)
                Toast.makeText(this, "Barbeiro cadastrado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
