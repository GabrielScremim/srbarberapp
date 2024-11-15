package com.github.gabrielscremim.srbarber1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.gabrielscremim.srbarber1.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnCadastrarBarbeiro).setOnClickListener {
            startActivity(Intent(this, CadastroBarbeiroActivity::class.java))
        }

        findViewById<Button>(R.id.btnCadastrarCliente).setOnClickListener {
            startActivity(Intent(this, CadastroClienteActivity::class.java))
        }

        findViewById<Button>(R.id.btnAgenda).setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
}
