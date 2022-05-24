package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.OrderAdapter
import com.example.veganhouse.model.Order
import com.example.veganhouse.service.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileOrders : Fragment() {

    lateinit var orderCard: RecyclerView
    lateinit var progressBar: ProgressBar

    var loggedUserId = 1
    var arrayOrders: ArrayList<Order> = arrayListOf()
    var adapter = OrderAdapter(arrayOrders, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile_orders, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        orderCard = v.findViewById<RecyclerView>(R.id.orders_component)
        orderCard.adapter = adapter
        progressBar = v.findViewById(R.id.progress_bar)

        getUserOrder()

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        var listBtnProfile: ArrayList<ImageButton> = arrayListOf(
            v.findViewById(R.id.btn_profile_data),
            v.findViewById(R.id.btn_profile_orders),
            v.findViewById(R.id.btn_profile_adress)
        )

        listBtnProfile.forEach { btn ->
            btn.setOnClickListener {
                when (btn.id) {
                    R.id.btn_profile_data -> transaction.replace(R.id.fl_wrapper, ProfilePersonalData::class.java, arguments)
                    R.id.btn_profile_orders ->  transaction.replace(R.id.fl_wrapper, ProfileOrders::class.java, arguments)
                    R.id.btn_profile_adress -> transaction.replace(R.id.fl_wrapper, ProfileAdressData::class.java, arguments)
                }
                transaction.commit()
            }
        }

    }

    fun showAlertDialog() {

        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder
            .setMessage("Sistema indisponível no momento. Por favor, tente mais tarde.")
            .setCancelable(true)
            .setPositiveButton("Ok, entendi!", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Atenção")
        alert.show()
    }

    private fun getUserOrder() {

        val getUserOrder = OrderService.getInstance().getUserOrder(loggedUserId)

        progressBar.visibility = View.VISIBLE

        getUserOrder.enqueue(object : Callback<List<Order>> {

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Usuário não tem pedidos", Toast.LENGTH_SHORT)
                            .show()
                        progressBar.visibility = View.GONE
                        return
                    }

                    if (arrayOrders.isNotEmpty()) arrayOrders.clear()
                    response.body()?.forEach { order ->
                        arrayOrders.add(order)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Usuário não tem pedidos", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                t.printStackTrace()
                progressBar.visibility = View.GONE
                showAlertDialog()
            }

        })

    }

}