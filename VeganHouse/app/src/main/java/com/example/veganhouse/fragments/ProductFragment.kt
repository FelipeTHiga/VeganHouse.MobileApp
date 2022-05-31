package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.ProductService
import com.example.veganhouse.R
import com.example.veganhouse.adapter.CertificationAdapter
import com.example.veganhouse.api.SellerCertifiedService
import com.example.veganhouse.model.Certification
import com.example.veganhouse.model.Product
import com.example.veganhouse.model.RestockNotification
import com.example.veganhouse.model.Seller
import com.example.veganhouse.service.EventManagerRestockService
import com.example.veganhouse.service.SellerService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : Fragment() {

    lateinit var ivProductImage: ImageView
    lateinit var tvProductName: TextView
    lateinit var tvProductSeller: TextView
    lateinit var tvProductPrice: TextView
    lateinit var tvProductDescription: TextView
    lateinit var tvSellerInstagram: TextView
    lateinit var tvSellerFacebook: TextView
    lateinit var tvSellerWhatsapp: TextView

    var productId = 1
    var productIsAvailable = true

    private var arrayCertification: ArrayList<Certification> = arrayListOf()
    private var adapter = CertificationAdapter(arrayCertification)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_product, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        if (arguments != null) {
            productId = arguments?.getInt("productId", 1)!!
            productIsAvailable = arguments?.getBoolean("productIsAvailable", true)!!
        }

        this.getProductById(productId)

        val btnOpenContainerCertifications: ImageView = v.findViewById(R.id.arrow_container_certifications)
        val btnOpenContainerSeller: ImageView = v.findViewById(R.id.arrow_container_seller)
        val btnRedirectCart: Button = v.findViewById(R.id.btn_add_cart)
        val btnNotifyUser: Button = v.findViewById(R.id.btn_notify_user)
        val tvNotifyUser: TextView = v.findViewById(R.id.tv_notify_user)

        ivProductImage = v.findViewById(R.id.product_image)
        tvProductName = v.findViewById(R.id.tv_product_name)
        tvProductSeller = v.findViewById(R.id.tv_product_seller)
        tvProductPrice = v.findViewById(R.id.tv_product_price)
        tvProductDescription = v.findViewById(R.id.tv_product_description)
        tvSellerInstagram = v.findViewById(R.id.tv_seller_instagram)
        tvSellerFacebook = v.findViewById(R.id.tv_seller_facebook)
        tvSellerWhatsapp = v.findViewById(R.id.tv_seller_whatsapp)

        v.findViewById<ImageView>(R.id.arrow_container_certifications).setImageResource(R.drawable.ic_arrow_right)
        v.findViewById<ImageView>(R.id.arrow_container_seller).setImageResource(R.drawable.ic_arrow_right)

        val recyclerView = v.findViewById<RecyclerView>(R.id.certifications_component)
        recyclerView.adapter = adapter

        btnOpenContainerCertifications.setOnClickListener {
            btnOpenContainerCertifications(v)
        }

        btnOpenContainerSeller.setOnClickListener {
            btnOpenContainerSeller(v)
        }

        btnRedirectCart.setOnClickListener {
            redirectCart()
        }

        tvNotifyUser.setOnClickListener {
            createSubscription()
        }

        if (productIsAvailable) {
            btnRedirectCart.visibility = View.VISIBLE
            btnNotifyUser.visibility = View.INVISIBLE
            tvNotifyUser.visibility = View.INVISIBLE
        } else {
            btnNotifyUser.visibility = View.VISIBLE
            tvNotifyUser.visibility = View.VISIBLE
            btnRedirectCart.visibility = View.INVISIBLE
        }
    }


    fun btnOpenContainerCertifications(v: View) {

        var arrow = v.findViewById<ImageView>(R.id.arrow_container_certifications)
        var containerCertification = v.findViewById<RelativeLayout>(R.id.rl_container_certification)

        if (containerCertification.visibility == View.VISIBLE) {
            containerCertification.visibility = View.GONE
            arrow.setImageResource(R.drawable.ic_arrow_right)
        } else {
            containerCertification.visibility = View.VISIBLE
            arrow.setImageResource(R.drawable.ic_arrow_down)
        }

    }

    fun btnOpenContainerSeller(v: View) {

        var arrow = v.findViewById<ImageView>(R.id.arrow_container_seller)
        var containerSocialMidia =
            v.findViewById<GridLayout>(R.id.grid_container_seller_social_midia)

        if (containerSocialMidia.visibility == View.VISIBLE) {
            containerSocialMidia.visibility = View.GONE
            arrow.setImageResource(R.drawable.ic_arrow_right)
        } else {
            containerSocialMidia.visibility = View.VISIBLE
            arrow.setImageResource(R.drawable.ic_arrow_down)
        }
    }

    fun redirectCart() {
        val cartFragment = CartFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, cartFragment)
        transaction.commit()
    }

    fun showAlertDialog() {

        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder
            .setMessage(getString(R.string.api_error_message))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ok_got_it), DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.attention))
        alert.show()
    }

    private fun getProductById(productId: Int) {

        val getProductById = ProductService.getInstance().getProductById(productId)

        getProductById.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                        return
                    }

                    var product = response.body()

                    tvProductName.text = product?.name
                    tvProductPrice.text = product?.price.toString()
                    tvProductDescription.text = product?.description
                    Picasso.get().load(getString(R.string.img_url_maquina3, productId, 1)).error(R.drawable.product_without_image).into(ivProductImage)


//                    if (product?.image_url1 == null) {
//                        ivProductImage.setImageResource(R.drawable.product_without_image)
//                    } else {
//                        val decodedString: ByteArray =
//                            Base64.decode(product?.image_url1, Base64.DEFAULT)
//                        val decodedByte =
//                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                        ivProductImage.setImageBitmap(decodedByte)
//                    }

                    var productSellerId = product?.fkSeller
                    if (productSellerId != null) {
                        getSellerById(productSellerId)
                        getSellerCertified(productSellerId)
                    }
                } else {
                    Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }

        })

    }

    private fun getSellerCertified(idSeller: Int) {

        val getSellerCertified = SellerCertifiedService.getInstance().getSellerCertified(idSeller)

        getSellerCertified.enqueue(object : Callback<List<Certification>> {

            override fun onResponse(call: Call<List<Certification>>, response: Response<List<Certification>>) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem certificados", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayCertification.isNotEmpty()) arrayCertification.clear()
                    response.body()?.forEach { certified ->
                        arrayCertification.add(certified)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Sem certificados", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Certification>>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }

        })

    }

    private fun getSellerById(idSeller: Int) {

        val getSellerById = SellerService.getInstance().getSellerById(idSeller)

        getSellerById.enqueue(object : Callback<Seller> {
            override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(context, "Vendedor não encontrado", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                    var seller = response.body()
                    tvProductSeller.text = seller?.commercialName
                    tvSellerInstagram.text = seller?.instagramAccount
                    tvSellerFacebook.text = seller?.facebookAccount
                    tvSellerWhatsapp.text = seller?.whatsappNumber
                }
            }

            override fun onFailure(call: Call<Seller>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }

        })
    }

    private fun createSubscription() {

        var restockNotification = RestockNotification(
            null,
            10,
            productId
        )

        val createSubscription = EventManagerRestockService.getInstance().createSubscription(restockNotification)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        createSubscription.enqueue(object : Callback<RestockNotification> {
            override fun onResponse(
                call: Call<RestockNotification>,
                response: Response<RestockNotification>
            ) {
                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle(getString(R.string.notify_by_email_title))
                        .setMessage(getString(R.string.notify_by_email_message))
                        .setCancelable(true)
                        .setPositiveButton(
                            getString(R.string.ok_got_it),
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            }).show()

                } else {
                    Toast.makeText(context, "Erro: ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<RestockNotification>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }

        })
    }
}