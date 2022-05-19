package com.example.veganhouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class NoConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
    }

    fun closeAplication(view: View) {
        finishAffinity()
    }

}