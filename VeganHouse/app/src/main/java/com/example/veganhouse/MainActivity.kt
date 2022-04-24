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

    fun telaCatalog(button: View) {

        var categoryPosition = 0
        var categoryValue = ""

        when (button.id) {
            R.id.btn_acessories -> {
                categoryPosition = 1
                categoryValue = "Acessórios"
            }
            R.id.btn_food -> {
                categoryPosition = 2
                categoryValue = "Alimentos"
            }
            R.id.btn_cosmetics -> {
                categoryPosition = 3
                categoryValue = "Cosméticos"
            }
            R.id.btn_health -> {
                categoryPosition = 4
                categoryValue = "Saúde"
            }
            R.id.btn_clothes -> {
                categoryPosition = 5
                categoryValue = "Vestimenta"
            }
            R.id.btn_explore -> {
                categoryPosition = 6
                categoryValue = "Todos"
            }
        }

        val telaCatalog = Intent(this, Catalog()::class.java)
        telaCatalog.putExtra("categoryPosition", categoryPosition)
        telaCatalog.putExtra("categoryValue", categoryValue)

        startActivity((telaCatalog))
    }
}