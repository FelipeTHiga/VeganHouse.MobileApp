package com.example.veganhouse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.veganhouse.api.ApiLogin
import com.example.veganhouse.data.User
import com.example.veganhouse.data.UserLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    lateinit var etEmail:EditText
    lateinit var etPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
    }

    fun login(v: View){
        val userLogin = UserLogin (etEmail.text.toString(), etPassword.text.toString())
        val loginUser = ApiLogin.criar().postLogin(userLogin);

        loginUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                    redirectHome(v)
                } else {
                    // etEmail.error = "Email incorreto"
                    // etPassword.error = "Senha incorreta"
                    Toast.makeText(baseContext, "Erro tentar realizar o login", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun redirectHome(v:View){
        val home = Intent(this, MainActivity::class.java)
        startActivity(home)
        Toast.makeText(baseContext, "Login realizado!", Toast.LENGTH_SHORT).show()
    }

    fun redirectSignin(v:View){
        val signin = Intent(this, Signin::class.java)
        startActivity(signin)
    }
}