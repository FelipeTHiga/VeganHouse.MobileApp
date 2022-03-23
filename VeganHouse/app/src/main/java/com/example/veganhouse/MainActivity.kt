package com.example.veganhouse

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
        Toast.makeText(this, "Mostrando menu", Toast.LENGTH_SHORT).show()
    }
}