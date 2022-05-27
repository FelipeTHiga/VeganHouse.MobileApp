package com.example.veganhouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R


class PaymentResultFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_payment_result, container, false)

        val btnReturnHome: Button = v.findViewById(R.id.btn_return_home)


        btnReturnHome.setOnClickListener {
            redirectHome()
        }

        return v
    }

    fun redirectHome(){
        val homeFragment = HomeFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, homeFragment)
        transaction.commit()
    }

}