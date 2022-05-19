package com.example.veganhouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.veganhouse.Activities.Login
import com.example.veganhouse.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // localstorage
    lateinit var preferences: SharedPreferences

    val homeFragment = HomeFragment()
    val loginFragment = LoginFragment()
    val catalogFragment = CatalogFragment()
    val paymentFragment = PaymentFragment()
    val cartFragment = CartFragment()

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

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.icon_home -> makeCurrentFragment(homeFragment)
                R.id.icon_user -> makeCurrentFragment(loginFragment)
                R.id.icon_shopping_bag -> makeCurrentFragment(cartFragment)
                R.id.icon_search -> makeCurrentFragment(catalogFragment)
            }
            true
        }

        checkNetworkConnection()

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

    fun showMenu(v: View) {
        val login = Intent(this, Login::class.java)
        startActivity(login)
        Toast.makeText(this, "Mostrando menu", Toast.LENGTH_SHORT).show()
    }

}

