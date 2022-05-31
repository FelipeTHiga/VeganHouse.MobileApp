package com.example.veganhouse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.fragments.CartFragment
import com.example.veganhouse.model.CartItem

class ProductDetailAdapter(private val cartItems: List<CartItem>, cartFragment: CartFragment) :
    RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_product_detail, parent, false)
        return ProductDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDetailViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class ProductDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CartItem) {
            with(itemView) {

                val tvProductName = findViewById<TextView>(R.id.tv_subtotal_name)
                val tvProductQuantity = findViewById<TextView>(R.id.tv_subtotal_quantity)
                val tvProductSubtotal = findViewById<TextView>(R.id.tv_subtotal_subtotal)

                tvProductName.text = data.product.name
                tvProductQuantity.text = data.quantity.toString()
                tvProductSubtotal.text = "R$ %.2f".format(data.subTotal)

            }
        }
    }
}