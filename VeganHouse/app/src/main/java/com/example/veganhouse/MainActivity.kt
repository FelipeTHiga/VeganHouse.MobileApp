package com.example.veganhouse

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.veganhouse.fragments.*

import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

 // localstorage
 lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences("user", MODE_PRIVATE)
        val auth = preferences.getString("id", null)

        if (!auth.isNullOrEmpty()) {
            var userFragment = when {
                !auth.isNullOrEmpty() -> LoginFragment()
                else -> HomeFragment() //ProfileFragment()
            }
        }

        val homeFragment = HomeFragment()
        val loginFragment = LoginFragment()
        val catalogFragment = CatalogFragment()
        val paymentFragment = PaymentFragment()
        val cartFragment = CartFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        makeCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> when(item.itemId){
            R.id.icon_home -> makeCurrentFragment(homeFragment)
            R.id.icon_user ->  makeCurrentFragment(loginFragment)
            R.id.icon_shopping_bag -> makeCurrentFragment(cartFragment)
            // R.id.icon_search -> makeCurrentFragment(catalogFragment)
        }
        true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply{
        replace(R.id.fl_wrapper, fragment)
        commit()
    }


    fun showMenu(v:View){
        val login = Intent(this,Login::class.java)
        startActivity(login)
        Toast.makeText(this, "Mostrando menu", Toast.LENGTH_SHORT).show()
    }

//    fun abrirTelaProduto(v:View){
//        val telaProduto = Intent(this, Product()::class.java)
//        startActivity((telaProduto))
//    }


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

//    fun telaCatalog(button: View) {
//
//        val bundle = Bundle()
//        val catalogFragment = CatalogFragment()
//        var categoryPosition = 0
//        var categoryValue = ""
//
//        when (button.id) {
//            R.id.btn_acessories -> {
//                categoryPosition = 1
//                categoryValue = "Acessórios"
//            }
//            R.id.btn_food -> {
//                categoryPosition = 2
//                categoryValue = "Alimentos"
//            }
//            R.id.btn_cosmetics -> {
//                categoryPosition = 3
//                categoryValue = "Cosméticos"
//            }
//            R.id.btn_health -> {
//                categoryPosition = 4
//                categoryValue = "Saúde"
//            }
//            R.id.btn_clothes -> {
//                categoryPosition = 5
//                categoryValue = "Vestimenta"
//            }
//            R.id.btn_explore -> {
//                categoryPosition = 6
//                categoryValue = "Todos"
//            }
//        }
//
//        bundle.putInt("categoryPosition", categoryPosition)
//        bundle.putString("categoryValue", categoryValue)
//        catalogFragment.arguments = bundle
//        makeCurrentFragment(catalogFragment)
//
//    }

}