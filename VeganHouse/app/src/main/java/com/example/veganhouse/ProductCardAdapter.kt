package com.example.veganhouse

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.data.Product

class ProductCardAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductCardAdapter.ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.product_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(data: Product){
            with(itemView){

                val product_image = findViewById<ImageView>(R.id.product_image)
                val tv_product_name = findViewById<TextView>(R.id.product_name)
                val tv_product_price = findViewById<TextView>(R.id.product_price)
                val tv_product_score = findViewById<TextView>(R.id.product_score)

//                product_image.setImageResource(R.drawable.product_without_image)
//                if (data.image_url1.size == 0) product_image.setImageResource(R.drawable.product_without_image) else
//                    product_image.setImageBitmap(BitmapFactory.decodeByteArray(data.image_url1, 0, data.image_url1.size))

                var byteArray: ByteArray = data.image_url1.encodeToByteArray()
                if (byteArray.size == 0) product_image.setImageResource(R.drawable.product_without_image) else
                    product_image.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))

                tv_product_name.text = data.name
                tv_product_price.text = data.price.toString()
                tv_product_score.text = 4.5.toString()

            }
        }
    }
}