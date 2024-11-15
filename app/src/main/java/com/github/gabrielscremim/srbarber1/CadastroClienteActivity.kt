package com.github.gabrielscremim.srbarber1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gabrielscremim.srbarber1.R

class CadastroClienteActivity : AppCompatActivity() {

    private lateinit var edtNomeCliente: EditText
    private lateinit var edtTelefoneCliente: EditText
    private lateinit var btnSalvarCliente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        edtNomeCliente = findViewById(R.id.edtNomeCliente)
        edtTelefoneCliente = findViewById(R.id.edtTelefoneCliente)
        btnSalvarCliente = findViewById(R.id.btnSalvarCliente)

        btnSalvarCliente.setOnClickListener {
            val nome = edtNomeCliente.text.toString()
            val telefone = edtTelefoneCliente.text.toString()

            if (nome.isNotEmpty() && telefone.isNotEmpty()) {
                // Salvando no banco de dados
                DatabaseHelper(this).addCliente(nome, telefone)
                Toast.makeText(this, "Cliente cadastrado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
