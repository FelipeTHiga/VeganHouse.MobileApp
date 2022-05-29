package com.example.veganhouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.CardCartAdapter
import com.example.veganhouse.adapter.ProductCardAdapter
import com.example.veganhouse.model.CartItem
import com.example.veganhouse.model.Product
import com.example.veganhouse.service.CartItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters

    var arrayCardCart: ArrayList<CartItem> = arrayListOf()
    var adapter = CardCartAdapter(arrayCardCart)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_cart, container, false)
        val cartCard = v.findViewById<RecyclerView>(R.id.card_cart_products)
        cartCard.adapter = adapter
        val btnPayment: Button = v.findViewById(R.id.btn_payment)


        getUserCartItems()

        btnPayment.setOnClickListener {
            redirectPayment()
        }


        return v
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}