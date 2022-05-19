package com.example.veganhouse.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.fragments.HomeFragment
import com.example.veganhouse.model.Product

class NewProductCardAdapter(private val products: List<Product>, homeFragment: HomeFragment) :
    RecyclerView.Adapter<NewProductCardAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewProductCardAdapter.ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.new_product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewProductCardAdapter.ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Product) {
            with(itemView) {

                val product_image = findViewById<ImageView>(R.id.product_image)
                val tv_product_name = findViewById<TextView>(R.id.product_name)
                val tv_product_price = findViewById<TextView>(R.id.product_price)

                tv_product_name.text = data.name
                tv_product_price.text = data.price.toString()

                if (data.image_url1 == null) {
                    product_image.setImageResource(R.drawable.product_without_image)
                } else {
                    val decodedString: ByteArray = Base64.decode(data.image_url1, Base64.DEFAULT)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    product_image.setImageBitmap(decodedByte)
                }
            }
        }
    }
}