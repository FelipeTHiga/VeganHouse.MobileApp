package com.example.veganhouse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.CardCartAdapter
import com.example.veganhouse.adapter.ProductAdapter
import com.example.veganhouse.model.CartItem
import com.example.veganhouse.model.Product
import com.example.veganhouse.service.CartItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    var arrayCardCart: ArrayList<CartItem> = arrayListOf()
    var adapter = CardCartAdapter(arrayCardCart)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_cart, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val cartCard = v.findViewById<RecyclerView>(R.id.card_cart_products)
        cartCard.adapter = adapter
        val btnPayment: Button = v.findViewById(R.id.btn_payment)


        getUserCartItems()

        btnPayment.setOnClickListener {
            redirectPayment()
        }

    }

    fun getUserCartItems() {
        val getUserCartItems = CartItemService.getInstance().getUserCartItems(1)

        getUserCartItems.enqueue(
            object : Callback<List<CartItem>> {

                override fun onResponse(
                    call: Call<List<CartItem>>,
                    response: Response<List<CartItem>>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 204 || response.body() == null) {
                            Toast.makeText(context, "O carrinho está vazio", Toast.LENGTH_SHORT)
                                .show()
                            return
                        }
                        if (arrayCardCart.isNotEmpty()) arrayCardCart.clear()
                        response.body()?.forEach { product ->
                            arrayCardCart.add(product)
                        }
                        adapter.notifyDataSetChanged()

                    } else {
                        Toast.makeText(context, "O carrinho está vazio", Toast.LENGTH_SHORT).show()
                    }

                }


                override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun redirectPayment() {
        val paymentFragment = PaymentFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, paymentFragment)
        transaction.commit()
    }

}