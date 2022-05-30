package com.example.veganhouse.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.model.Product
import android.util.Base64
import com.example.veganhouse.fragments.CatalogFragment


class ProductAdapter(private val products: List<Product>, private val listener: CatalogFragment) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(data: Product) {
            with(itemView) {

                val product_image = findViewById<ImageView>(R.id.product_image)
                val tv_product_name = findViewById<TextView>(R.id.product_name)
                val tv_product_price = findViewById<TextView>(R.id.product_price)
                val tv_product_score = findViewById<TextView>(R.id.product_score)

                tv_product_name.text = data.name
                tv_product_price.text = "R$ %.2f".format(data.price)
                tv_product_score.text = 4.5.toString()

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

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}