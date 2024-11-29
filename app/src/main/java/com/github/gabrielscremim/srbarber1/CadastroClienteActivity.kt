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

class CadastroClienteActivity : AppCompatActivity() {

    private lateinit var edtNomeCliente: EditText
    private lateinit var edtEmailCliente: EditText
    private lateinit var edtSenhaCliente: EditText
    private lateinit var btnSalvarCliente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        edtNomeCliente = findViewById(R.id.edtNomeCliente)
        edtEmailCliente = findViewById(R.id.edtEmailCliente)
        edtSenhaCliente = findViewById(R.id.edtSenhaCliente)
        btnSalvarCliente = findViewById(R.id.btnSalvarCliente)

        btnSalvarCliente.setOnClickListener {
            val nome = edtNomeCliente.text.toString()
            val email = edtEmailCliente.text.toString()
            val senha = edtSenhaCliente.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()) {
                fazerLogin { token ->
                    if (token != null) {
                        buscarUltimoId("usuarios", token) { ultimoId ->
                            if (ultimoId != null) {
                                cadastrarCliente(token, ultimoId + 1, nome, email, senha)
                            } else {
                                Toast.makeText(this, "Erro ao buscar o último ID", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Erro no login", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fazerLogin(callback: (String?) -> Unit) {
        val dadosLogin = mapOf("username" to "trabalho-mobile-barbearia")
        ConexaoAPI.api.fazerLogin(dadosLogin).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val token = response.body()?.get("token")
                    Log.d("LOGIN", "Token recebido: $token")
                    callback(token)
                } else {
                    Log.e("LOGIN", "Erro no login. Código: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("LOGIN", "Erro na conexão: ${t.message}")
                callback(null)
            }
        })
    }

    private fun buscarUltimoId(tabela: String, token: String, callback: (Int?) -> Unit) {
        Log.d("BUSCAR_ID", "Buscando último ID para tabela: $tabela com token: $token")

        ConexaoAPI.api.buscarDados("Bearer $token").enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    val dados = response.body()
                    Log.d("BUSCAR_ID", "Dados recebidos: $dados")

                    // Filtra registros pela tabela especificada
                    val registros = dados?.filter { registro ->
                        val data = registro["data"] as? Map<*, *>
                        data?.get("tabela") == tabela
                    }

                    Log.d("BUSCAR_ID", "Registros encontrados para tabela $tabela: $registros")

                    // Extrai os IDs do "dado" dentro dos registros
                    val ids = registros?.flatMap { registro ->
                        val data = registro["data"] as? Map<*, *>
                        val dadosRegistro = data?.get("dado") as? List<Map<String, Any>>
                        dadosRegistro?.mapNotNull { dado ->
                            (dado["id"] as? Number)?.toInt() // Extrai e converte o ID
                        } ?: emptyList()
                    }

                    val ultimoId = ids?.maxOrNull()
                    Log.d("BUSCAR_ID", "Último ID encontrado: $ultimoId")
                    callback(ultimoId)
                } else {
                    Log.e("BUSCAR_ID", "Erro ao buscar dados. Código: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.e("BUSCAR_ID", "Erro na conexão: ${t.message}")
                callback(null)
            }
        })
    }

    private fun cadastrarCliente(token: String, id: Int, nome: String, email: String, senha: String) {
        val dadosCliente = mapOf(
            "tabela" to "usuarios",
            "dado" to listOf(
                mapOf(
                    "id" to id,
                    "nome" to nome,
                    "email" to email,
                    "senha" to senha
                )
            )
        )

        Log.d("CADASTRO_CLIENTE", "JSON Enviado: $dadosCliente")

        ConexaoAPI.api.salvarBarbeiro("Bearer $token", dadosCliente).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CADASTRO_CLIENTE", "Usuário cadastrado com sucesso.")
                    Toast.makeText(this@CadastroClienteActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    voltarParaTelaPrincipal()
                } else {
                    Log.e("CADASTRO_CLIENTE", "Erro ao cadastrar. Código: ${response.code()}")
                    Toast.makeText(this@CadastroClienteActivity, "Erro ao cadastrar o usuário", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CADASTRO_CLIENTE", "Erro na conexão: ${t.message}")
                Toast.makeText(this@CadastroClienteActivity, "Erro na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun voltarParaTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}