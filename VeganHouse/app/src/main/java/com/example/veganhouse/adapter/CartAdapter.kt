package com.example.veganhouse.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.fragments.CartFragment
import com.example.veganhouse.model.CartItem
import com.example.veganhouse.service.CartItemService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(private val cartItems: List<CartItem>, val cartFragment: CartFragment) :
    RecyclerView.Adapter<CartAdapter.CardCartViewHolder>() {

    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        view = layoutInflater.inflate(R.layout.card_cart_item, parent, false)

        return CardCartViewHolder(view)

    }

    override fun onBindViewHolder(holder: CardCartViewHolder, position: Int) {
        holder.bind(cartItems[position])

        holder.itemView.findViewById<Button>(R.id.btn_plus).setOnClickListener {

            val incrementCartItemQuantity = CartItemService.getInstance().incrementCartItemQuantity(cartItems[position].idCartItem)

            incrementCartItemQuantity.enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful) {
                        val transaction = (view.context as FragmentActivity).supportFragmentManager?.beginTransaction()
                        transaction.detach(cartFragment)
                        transaction.attach(cartFragment)
                        transaction.commit()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }

        holder.itemView.findViewById<Button>(R.id.btn_minus).setOnClickListener {

            val decrementCartItemQuantity = CartItemService.getInstance().decrementCartItemQuantity(cartItems[position].idCartItem)

            decrementCartItemQuantity.enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful) {
                        val transaction = (view.context as FragmentActivity).supportFragmentManager?.beginTransaction()
                        transaction.detach(cartFragment)
                        transaction.attach(cartFragment)
                        transaction.commit()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }

            })

        }

        holder.itemView.findViewById<ImageView>(R.id.iv_btn_close).setOnClickListener {

            val removeCartItem =
                CartItemService.getInstance().removeCartItem(cartItems[position].idCartItem)

            removeCartItem.enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val transaction = (view.context as FragmentActivity).supportFragmentManager?.beginTransaction()
                        transaction.detach(cartFragment)
                        transaction.attach(cartFragment)
                        transaction.commit()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        }

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CardCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CartItem) {
            with(itemView) {

                val cardCartImg = findViewById<ImageView>(R.id.iv_product_image)
                val cardCartName = findViewById<TextView>(R.id.tv_product_name)
                val cardCartPrice = findViewById<TextView>(R.id.tv_product_price)
                val cardCartQt = findViewById<TextView>(R.id.tv_product_quantity)
                val cardCartSubtotal = findViewById<TextView>(R.id.tv_subtotal)

//                if (data.product.image_url1 == null) {
//                    cardCartImg.setImageResource(R.drawable.product_without_image)
//                } else {
//                    val decodedString: ByteArray =
//                        Base64.decode(data.product.image_url1, Base64.DEFAULT)
//                    val decodedByte =
//                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                    cardCartImg.setImageBitmap(decodedByte)
//                }

                val id = data.product.id

                Picasso.get().load("https://veganhouseback.ddns.net/products/images/$id/1").error(R.drawable.product_without_image).into(cardCartImg)

                cardCartName.text = data.product.name
                cardCartPrice.text = "R$ %.2f".format(data.product.price)
                cardCartQt.text = data.quantity.toString()
                cardCartSubtotal.text = "Subtotal: R$ %.2f".format(data.quantity * data.product.price)

            }

        }

    }


}

