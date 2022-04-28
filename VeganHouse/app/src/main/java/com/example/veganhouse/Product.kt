package com.example.veganhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView


class Product : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

    }

    fun openMenuRedesSociais(v:View){

        var arrow = findViewById<ImageView>(R.id.iconArrow)
        var instagram = findViewById<ImageView>(R.id.iconInstagram)
        var facebook = findViewById<ImageView>(R.id.iconFacebook)
        var whatsapp = findViewById<ImageView>(R.id.iconWhatsapp)

        if(instagram.visibility == View.VISIBLE){
            instagram.visibility = View.GONE
            facebook.visibility = View.GONE
            whatsapp.visibility = View.GONE
        } else {
            instagram.visibility = View.VISIBLE
            facebook.visibility = View.VISIBLE
            whatsapp.visibility = View.VISIBLE
        }
    }


    fun openMenuSelos(v:View){

        var arrow = findViewById<ImageView>(R.id.iconArrow)

        var containerImage = findViewById<RelativeLayout>(R.id.imageContainer)

            if (containerImage.visibility == View.VISIBLE){
                containerImage.visibility = View.GONE
            } else {
                containerImage.visibility = View.VISIBLE
            }

//        var selo1 = findViewById<ImageView>(R.id.selo1)
//        var selo2 = findViewById<ImageView>(R.id.selo2)
//        var selo3 = findViewById<ImageView>(R.id.selo3)
//
//        var textSelo1 = findViewById<TextView>(R.id.textSelo1)
//        var textSelo2 = findViewById<TextView>(R.id.textSelo2)
//        var textSelo3 = findViewById<TextView>(R.id.textSelo3)
//
//        if(selo1.visibility == View.VISIBLE){
//            selo1.visibility = View.GONE
//            selo2.visibility = View.GONE
//            selo3.visibility = View.GONE
//
//            textSelo1.visibility = View.GONE
//            textSelo2.visibility = View.GONE
//            textSelo3.visibility = View.GONE
//
//        } else {
//            selo1.visibility = View.VISIBLE
//            selo2.visibility = View.VISIBLE
//            selo3.visibility = View.VISIBLE
//
//            textSelo1.visibility = View.VISIBLE
//            textSelo2.visibility = View.VISIBLE
//            textSelo3.visibility = View.VISIBLE
//        }

    }

    fun redirectCart(v:View){
        val cartActivity = Intent(this, Cart::class.java)
        startActivity(cartActivity)
    }



}