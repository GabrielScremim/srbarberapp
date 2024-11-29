package com.github.gabrielscremim.srbarber1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class CadastroBarbeiroActivity : AppCompatActivity() {

    private lateinit var campoNomeBarbeiro: EditText
    private lateinit var botaoSalvarBarbeiro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_barbeiro)

        Log.d("CADASTRO_ACTIVITY", "Activity iniciada com sucesso.")

        campoNomeBarbeiro = findViewById(R.id.edtNomeBarbeiro)
        botaoSalvarBarbeiro = findViewById(R.id.btnSalvarBarbeiro)

        botaoSalvarBarbeiro.setOnClickListener {
            val nome = campoNomeBarbeiro.text.toString()

            if (nome.isNotEmpty()) {
                Log.d("CADASTRO_ACTIVITY", "Nome do barbeiro: $nome")
                fazerLogin { token ->
                    if (token != null) {
                        buscarUltimoId(token) { ultimoId ->
                            if (ultimoId != null) {
                                cadastrarBarbeiro(token, ultimoId + 1, nome)
                            } else {
                                Log.e("CADASTRO_ACTIVITY", "Erro ao buscar o último ID.")
                                Toast.makeText(this, "Erro ao buscar o último ID", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("CADASTRO_ACTIVITY", "Falha no login.")
                        Toast.makeText(this, "Erro no login", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha o nome do barbeiro!", Toast.LENGTH_SHORT).show()
                Log.e("CADASTRO_ACTIVITY", "Campo nome vazio.")
            }
        }
    }

    private fun fazerLogin(callback: (String?) -> Unit) {
        val dadosLogin = mapOf("username" to "trabalho-mobile-barbearia")
        Log.d("LOGIN", "Tentando login com usuário: trabalho-mobile-barbearia")

        ConexaoAPI.api.fazerLogin(dadosLogin).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val token = response.body()?.get("token")
                    Log.d("LOGIN", "Login bem-sucedido. Token: $token")
                    callback(token)
                } else {
                    Log.e("LOGIN", "Erro no login. Código: ${response.code()} Mensagem: ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("LOGIN", "Falha na conexão: ${t.message}")
                callback(null)
            }
        })
    }

    private fun buscarUltimoId(token: String, callback: (Int?) -> Unit) {
        Log.d("BUSCAR_ID", "Buscando último ID com token: $token")

        ConexaoAPI.api.buscarDados("Bearer $token").enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    Log.d("BUSCAR_ID", "Dados recebidos com sucesso.")
                    val dados = response.body()
                    val funcionarios = dados?.filter {
                        it["data"]?.let { data -> (data as Map<*, *>)["tabela"] == "funcionarios" } == true
                    }
                    val ids = funcionarios?.flatMap { funcionario ->
                        val dadosFuncionario = (funcionario["data"] as Map<*, *>)["dado"] as List<Map<String, Any>>
                        dadosFuncionario.mapNotNull {
                            (it["id"] as? Number)?.toInt() // Trata números como Double ou Int
                        }
                    }
                    Log.d("BUSCAR_ID", "Último ID encontrado: ${ids?.maxOrNull()}")
                    callback(ids?.maxOrNull())
                } else {
                    Log.e("BUSCAR_ID", "Erro ao buscar dados. Código: ${response.code()} Mensagem: ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.e("BUSCAR_ID", "Falha na conexão: ${t.message}")
                callback(null)
            }
        })
    }

    private fun cadastrarBarbeiro(token: String, id: Int, nome: String) {
        val dadosBarbeiro = mapOf(
            "tabela" to "funcionarios",
            "dado" to listOf(
                mapOf(
                    "id" to id,
                    "nome" to nome
                )
            )
        )

        Log.d("CADASTRO_BARBEIRO", "JSON enviado: $dadosBarbeiro")

        ConexaoAPI.api.salvarBarbeiro("Bearer $token", dadosBarbeiro).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CADASTRO_BARBEIRO", "Barbeiro cadastrado com sucesso.")
                    Toast.makeText(this@CadastroBarbeiroActivity, "Barbeiro cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    voltarParaTelaPrincipal()
                } else {
                    Log.e("CADASTRO_BARBEIRO", "Erro ao cadastrar. Código: ${response.code()} Mensagem: ${response.message()}")
                    Toast.makeText(this@CadastroBarbeiroActivity, "Erro ao cadastrar o barbeiro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CADASTRO_BARBEIRO", "Erro na conexão: ${t.message}")
                Toast.makeText(this@CadastroBarbeiroActivity, "Erro na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun voltarParaTelaPrincipal() {
        try {
            Log.d("VOLTA_MAIN", "Voltando para MainActivity.")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("VOLTA_MAIN", "Erro ao voltar para MainActivity: ${e.message}")
        }
    }
}