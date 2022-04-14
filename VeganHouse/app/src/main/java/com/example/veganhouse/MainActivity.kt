package com.example.veganhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    fun showMenu(v:View){
        val login = Intent(this,Login::class.java)
        startActivity(login)
        Toast.makeText(this, "Mostrando menu", Toast.LENGTH_SHORT).show()
    }

    fun testPayment(v:View){
        val telaPaymentResult = Intent(this, PaymentResult::class.java)
        startActivity(telaPaymentResult)
    }
}