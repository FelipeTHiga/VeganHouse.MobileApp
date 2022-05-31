package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.CartAdapter
import com.example.veganhouse.adapter.ProductDetailAdapter
import com.example.veganhouse.model.CartItem
import com.example.veganhouse.service.CartItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    lateinit var progressBar: ProgressBar
    lateinit var tvDefaultMessage: TextView
    lateinit var tvTotalCart: TextView
    lateinit var preferences: SharedPreferences
    var loggedUserId = 0
    var totalCart: Double = 0.00

    var arrayCardCart: ArrayList<CartItem> = arrayListOf()
    var adapter = CartAdapter(arrayCardCart, this)
    var adapterProductDetail = ProductDetailAdapter(arrayCardCart, this)

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

        preferences =
            activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!
        loggedUserId = preferences.getInt("id", 0)

        tvTotalCart = v.findViewById(R.id.tv_total_cart)
        progressBar = v.findViewById(R.id.progress_bar)
        tvDefaultMessage = v.findViewById(R.id.tv_default_message)

        getUserCartItems(loggedUserId)

        val cartCard = v.findViewById<RecyclerView>(R.id.card_cart_products)
        cartCard.adapter = adapter
        val productDetailLine = v.findViewById<RecyclerView>(R.id.container_product_detail)
        productDetailLine.adapter = adapterProductDetail
        val btnPayment: Button = v.findViewById(R.id.btn_payment)

        btnPayment.setOnClickListener {
            redirectPayment()
        }

    }

    fun showAlertDialog() {

        val dialogBuilder = android.app.AlertDialog.Builder(context)

        dialogBuilder
            .setTitle(getString(R.string.attention))
            .setMessage(getString(R.string.api_error_message))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.ok_got_it),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                }).show()

    }

    fun getUserCartItems(idUser: Int) {

        val getUserCartItems = CartItemService.getInstance().getUserCartItems(idUser)

        progressBar.visibility = View.VISIBLE

        getUserCartItems.enqueue(
            object : Callback<List<CartItem>> {

                override fun onResponse(
                    call: Call<List<CartItem>>,
                    response: Response<List<CartItem>>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 204 || response.body() == null) {
                            arrayCardCart.clear()
                            adapter.notifyDataSetChanged()
                            adapterProductDetail.notifyDataSetChanged()
                            tvDefaultMessage.text = getString(R.string.empty_cart)
                            progressBar.visibility = View.GONE
                            return
                        }
                        if (arrayCardCart.isNotEmpty()) arrayCardCart.clear()
                        totalCart = 0.0
                        response.body()?.forEach { cartItem ->
                            arrayCardCart.add(cartItem)
                            totalCart += cartItem.subTotal!!
                        }
                        adapter.notifyDataSetChanged()
                        adapterProductDetail.notifyDataSetChanged()
                        tvDefaultMessage.text = ""
                        progressBar.visibility = View.GONE
                        tvTotalCart.text = "R$ %.2f".format(totalCart)
                    } else {
                        tvDefaultMessage.text = getString(R.string.empty_cart)
                        progressBar.visibility = View.GONE
                    }

                }

                override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                    t.printStackTrace()
                    showAlertDialog()
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