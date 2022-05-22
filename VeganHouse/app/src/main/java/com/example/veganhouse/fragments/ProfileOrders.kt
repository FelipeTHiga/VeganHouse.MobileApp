package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.adapter.OrderAdapter
import com.example.veganhouse.model.Order
import com.example.veganhouse.model.Product
import com.example.veganhouse.model.User
import com.example.veganhouse.service.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOrders.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOrders : Fragment() {

    lateinit var orderCard: RecyclerView


    val lary = User(
        1, "Dino", "Sauro", "62795628082", "dino@gmail.com",
        "sonho2022", true, true, null
    )

    var orderItems: ArrayList<com.example.veganhouse.model.CartItem> = arrayListOf(
            com.example.veganhouse.model.CartItem(9,
            Product(1, "Óleo Vegetal De Romã", 75.00, "health", "óleo para pele", 10, 1, "", "", "", true),
            2, 33.98, 1, 7)
    )

    var arrayOrders: ArrayList<Order> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            arrayListOf(
                Order(7, lary, "Praça Benedito Calixto, nº162 - Pinheiros, São Paulo - 05406040",408.98, orderItems,
                    LocalDate.parse("2022-05-12", DateTimeFormatter.ISO_DATE), "Pendente")
            )
        } else {
            arrayListOf(
                Order(7, lary, "Praça Benedito Calixto, nº162 - Pinheiros, São Paulo - 05406040",408.98, orderItems,
                   null, "Pendente")
            )
        }

    val adapter = OrderAdapter(arrayOrders, this)

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

        getUserOrder()
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

        val getUserOrder = OrderService.getInstance().getUserOrder(1)

        getUserOrder.enqueue(object : Callback<List<Order>> {

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Usuário não tem pedidos", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    if (arrayOrders.isNotEmpty()) arrayOrders.clear()
                    response.body()?.forEach { order ->
                        arrayOrders.add(order)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Usuário não tem pedidos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileOrders.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileOrders().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}