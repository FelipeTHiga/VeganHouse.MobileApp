package com.example.veganhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.veganhouse.service.UserService
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signin : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etSurname:EditText
    lateinit var etEmail:EditText
    lateinit var etPassword:EditText
    lateinit var etCpf:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        etName = findViewById(R.id.et_name)
        etSurname = findViewById(R.id.et_surname)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etCpf = findViewById(R.id.et_cpf)
    }

    fun registerUser(v:View) {
        var newUser = UserRegister(
            etName.text.toString(),
            etSurname.text.toString(),
            etCpf.text.toString(),
            etEmail.text.toString(),
            etPassword.text.toString(),
            false
        )

        val apiRegister = UserService.getInstance().resgiterUser(newUser)

        apiRegister.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    redirectLogin(v)
                } else {
                    Toast.makeText(baseContext, "Erro tentar realizar o cadastro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun redirectLogin(v:View){
        val login = Intent(this, Login::class.java)
        startActivity(login)
        Toast.makeText(baseContext, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
    }
}