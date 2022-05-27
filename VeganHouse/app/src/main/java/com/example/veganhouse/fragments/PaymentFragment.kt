package com.example.veganhouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R

class PaymentFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_payment, container, false)

        val btnPaymentResult: Button = v.findViewById(R.id.btn_payment_result)

        btnPaymentResult.setOnClickListener {
            redirectPaymentResult()
        }

        return v
    }

    fun redirectPaymentResult(){
        val paymentResultFragment = PaymentResultFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, paymentResultFragment)
        transaction.commit()
    }

}