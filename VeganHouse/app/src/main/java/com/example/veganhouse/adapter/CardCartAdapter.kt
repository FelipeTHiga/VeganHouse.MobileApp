package com.example.veganhouse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.model.CartItem
import com.example.veganhouse.service.CartItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CardCartAdapter(
    private val cartItems: List<CartItem>
) :
    RecyclerView.Adapter<CardCartAdapter.CardCartViewHolder>() {

    var number: Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.fragment_card_cart, parent, false)


        return CardCartViewHolder(view)

    }


    override fun onBindViewHolder(holder: CardCartViewHolder, position: Int) {
        holder.bind(cartItems[position])

        holder.itemView.findViewById<Button>(R.id.btn_plus).setOnClickListener {
            number = number + 1
            holder.itemView.findViewById<TextView>(R.id.tv_product_quantity).text =
                number.toString()
        }

        holder.itemView.findViewById<Button>(R.id.btn_minus).setOnClickListener {
            if (number > 1) {
                number = number - 1
                holder.itemView.findViewById<TextView>(R.id.tv_product_quantity).text =
                    number.toString()
            }

        }

        holder.itemView.findViewById<ImageView>(R.id.iv_btn_close).setOnClickListener {

            val removeCartItem = CartItemService.getInstance().removeCartItem(1)


        }

    }



override fun getItemCount(): Int {
    return cartItems.size
}


class CardCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    fun bind(data: CartItem) {
        with(itemView) {


            val cardCartName = findViewById<TextView>(R.id.tv_product_name)
            val cardCartPrice = findViewById<TextView>(R.id.tv_product_price)
            val cardCartQt = findViewById<TextView>(R.id.tv_product_quantity)
            val cardCartSubtotal = findViewById<TextView>(R.id.tv_subtotal)
            val cardCartImg = findViewById<ImageView>(R.id.iv_product_image)


            cardCartImg.setImageResource(R.drawable.product_without_image)
            cardCartName.text = data.product.name
            cardCartPrice.text = data.product.price.toString()
            cardCartQt.text = data.quantity.toString()
            cardCartSubtotal.text = (data.quantity * data.product.price).toString()


        }

    }


}


}

