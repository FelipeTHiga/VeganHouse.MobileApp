package com.example.veganhouse.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.fragments.ProfileOrders
import com.example.veganhouse.model.Order
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderAdapter(private val orders: List<Order>, profileOrders: ProfileOrders) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.order_product_item, parent, false)
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
                val order_image = findViewById<ImageView>(R.id.product_image)

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

//                if (data.orderItems[0].product.image_url1 == null) {
//                    order_image.setImageResource(R.drawable.product_without_image)
//                } else {
//                    val decodedString: ByteArray = Base64.decode(data.orderItems[0].product.image_url1, Base64.DEFAULT)
//                    val decodedByte =
//                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                    order_image.setImageBitmap(decodedByte)
//                }

            }
        }
    }

}