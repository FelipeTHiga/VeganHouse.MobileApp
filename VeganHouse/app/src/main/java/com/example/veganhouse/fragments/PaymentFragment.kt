package com.example.veganhouse.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.model.Order
import com.example.veganhouse.R
import com.example.veganhouse.model.User
import com.example.veganhouse.service.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    lateinit var totalValue: TextView
    lateinit var preferences: SharedPreferences
     var idUser : Int = 0
//     lateinit var listOrder : List<com.example.veganhouse.model.Order>
     lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        preferences =
            activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!
        idUser = preferences.getInt("id", 0)

        getOrders()
        val v = inflater.inflate(R.layout.fragment_payment, container, false)
        totalValue = v.findViewById(R.id.tv_total_payment)
        val btnPaymentResult: Button = v.findViewById(R.id.btn_payment_result)
        totalValue.text = arguments?.getDouble("totalCart", 0.0).toString()





        btnPaymentResult.setOnClickListener {
            aprovedPayment()
        }
        return v
    }

    fun redirectPaymentResult(){
        val paymentResultFragment = PaymentResultFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, paymentResultFragment)
        transaction.commit()
    }

    fun getOrders(){
//        val getUserOrder = OrderService.getInstance().getUserOrder(idUser)
        val getOrder = OrderService.getInstance().getOrder(idUser)

        getOrder.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful){
                    order = response.body()!!

                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                t.printStackTrace()
            }
        })



//        getUserOrder.enqueue(object : Callback<List<Order>> {
//            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
//                if (response.isSuccessful){
//
//                    response.body()?.forEach { order ->
//                        listOrder.add(order)
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })

    }

    fun aprovedPayment(){
        val putOrder = OrderService.getInstance().updateOrder(idUser)

        putOrder.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    updateOrdedr(order.idOrder)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    fun updateOrdedr(idOrder : Int){
        val statusPayment = "Pagamento aprovado"
        val pachOrder = OrderService.getInstance().pacthOrder(statusPayment, idOrder)

        pachOrder.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    paymentResult()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun statusOrders(){
//        for (item : Order in listOrder) {
//            updateOrdedr(item.idOrder)
//        }
        paymentResult()
    }

    fun paymentResult(){
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        transaction.replace(R.id.fl_wrapper, PaymentResultFragment::class.java, arguments)
        transaction.commit()

    }





}