package com.example.veganhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.veganhouse.fragments.HomeFragment
import com.example.veganhouse.fragments.LoginFragment
import com.example.veganhouse.fragments.PaymentFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val loginFragment = LoginFragment()
        val paymentFragment = PaymentFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        makeCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> when(item.itemId){
            R.id.icon_home -> makeCurrentFragment(homeFragment);
            R.id.icon_user -> makeCurrentFragment(loginFragment)
            R.id.icon_shopping_bag -> makeCurrentFragment(paymentFragment)
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

}