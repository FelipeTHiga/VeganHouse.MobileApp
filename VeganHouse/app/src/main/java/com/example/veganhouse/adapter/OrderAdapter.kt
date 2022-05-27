package com.example.veganhouse.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.fragments.ProfileOrdersFragment
import com.example.veganhouse.model.Order

class OrderAdapter(private val orders: List<Order>, profileOrders: ProfileOrdersFragment) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_order_product, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Order) {
            with(itemView){

                val tv_order_id = findViewById<TextView>(R.id.tv_order_id)
                val tv_order_data = findViewById<TextView>(R.id.tv_order_data)
                val tv_order_total = findViewById<TextView>(R.id.tv_order_total)
                val tv_order_status = findViewById<TextView>(R.id.tv_order_status)

                tv_order_id.text = "#" + data.idOrder.toString()
                tv_order_data.text = data.orderDate
                tv_order_total.text = "R$" + data.total.toString()

                var color = when (data.orderStatus) {
                    "Pagamento aprovado" -> "#14E07E"
                    "Pagamento rejeitado" -> "#FF0000"
                    else -> "#8080FD"
                }

                tv_order_status.setTextColor(Color.parseColor(color))
                tv_order_status.text = data.orderStatus.toUpperCase()

            }
        }
    }

}