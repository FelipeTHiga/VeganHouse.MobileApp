package com.example.veganhouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.veganhouse.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // localstorage
    lateinit var preferences: SharedPreferences

    val homeFragment = HomeFragment()
    val catalogFragment = CatalogFragment()
    val cartFragment = CartFragment()
    val profilePersonalData = ProfilePersonalDataFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.icon_home -> makeCurrentFragment(homeFragment)
                R.id.icon_user -> checkIsLogged(profilePersonalData)
                R.id.icon_shopping_bag -> checkIsLogged(cartFragment)
                R.id.icon_search -> makeCurrentFragment(catalogFragment)
            }
            true
        }

        checkNetworkConnection()

    }

    private fun checkIsLogged(target: Fragment) {

        //val dialogBuilder = android.app.AlertDialog.Builder(applicationContext)
        preferences = getSharedPreferences("user", MODE_PRIVATE)
        val auth = preferences.getInt("id", 0)

        if (auth == 0) {
//            dialogBuilder
//                .setTitle("Você precisa estar logado para acessar essa funcionalidade")
//                .setPositiveButton("Ir para Login") { dialog, _ ->
//                    makeCurrentFragment(LoginFragment())
//                }.show()
            Toast.makeText(baseContext, "Você precisa estar logado para acessar essa funcionalidade", Toast.LENGTH_SHORT).show()
            makeCurrentFragment(LoginFragment())
        } else {
            makeCurrentFragment(target)
        }

    }

    private fun checkNetworkConnection() {

        val connectivityManager =
            this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getActiveNetworkInfo()

        if (networkInfo != null && networkInfo.isConnected()) {
            makeCurrentFragment(homeFragment)
        } else {
            startActivity(Intent(this, NoConnection::class.java))
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

}

