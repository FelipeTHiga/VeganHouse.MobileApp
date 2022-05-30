package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.OrderAdapter
import com.example.veganhouse.model.Order
import com.example.veganhouse.service.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileOrdersFragment : Fragment() {

    lateinit var orderCard: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var tvDefaultMessage: TextView
    lateinit var preferences: SharedPreferences

    var loggedUserId = 0

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

        preferences =
            activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!
        loggedUserId = preferences.getInt("id", 0)

        orderCard = v.findViewById<RecyclerView>(R.id.orders_component)
        orderCard.adapter = adapter
        progressBar = v.findViewById(R.id.progress_bar)
        tvDefaultMessage = v.findViewById(R.id.tv_default_message)

        getUserOrder(loggedUserId)

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
                    R.id.btn_profile_data -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfilePersonalDataFragment::class.java,
                        arguments
                    )
                    R.id.btn_profile_orders -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfileOrdersFragment::class.java,
                        arguments
                    )
                    R.id.btn_profile_adress -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfileAdressDataFragment::class.java,
                        arguments
                    )
                }
                transaction.commit()
            }
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

    private fun getUserOrder(idUser: Int) {

        val getUserOrder = OrderService.getInstance().getUserOrder(loggedUserId)

        progressBar.visibility = View.VISIBLE

        getUserOrder.enqueue(object : Callback<List<Order>> {

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        tvDefaultMessage.text = getString(R.string.without_orders)
                        progressBar.visibility = View.GONE
                        return
                    }

                    if (arrayOrders.isNotEmpty()) arrayOrders.clear()
                    response.body()?.forEach { order ->
                        arrayOrders.add(order)
                    }
                    adapter.notifyDataSetChanged()
                    tvDefaultMessage.text = ""
                    progressBar.visibility = View.GONE
                } else {
                    tvDefaultMessage.text = getString(R.string.without_orders)
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