package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.example.veganhouse.adapter.CertificationItemAdapter
import com.example.veganhouse.api.SellerCertifiedService
import com.example.veganhouse.model.Certification
import com.example.veganhouse.model.Product
import com.example.veganhouse.model.RestockNotification
import com.example.veganhouse.model.Seller
import com.example.veganhouse.service.EventManagerRestockService
import com.example.veganhouse.service.SellerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    private var adapter = CertificationItemAdapter(arrayCertification)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val btnOpenContainerCertifications: ImageView =
            v.findViewById(R.id.arrow_container_certifications)
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

        v.findViewById<ImageView>(R.id.arrow_container_certifications)
            .setImageResource(R.drawable.ic_arrow_right)
        v.findViewById<ImageView>(R.id.arrow_container_seller)
            .setImageResource(R.drawable.ic_arrow_right)

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

    fun showAlertDialog(title: String, message: String, btnText: String) {

        val dialogBuilder = AlertDialog.Builder(context) // , R.style.AlertDialogTheme

        dialogBuilder
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(btnText, DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }

    fun getSellerCertified(idSeller: Int) {

        val getSellerCertified = SellerCertifiedService.criar().getSellerCertified(idSeller)
        //val homedateList: List<HomeDate> = gson.fromJson(body, Array<HomeDate>::class.java).toList()

        getSellerCertified.enqueue(object : Callback<ArrayList<Certification>> {

            override fun onResponse(
                call: Call<ArrayList<Certification>>,
                response: Response<ArrayList<Certification>>
            ) {
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

            override fun onFailure(call: Call<ArrayList<Certification>>, t: Throwable) {
                t.printStackTrace()
                var title = getString(R.string.attention)
                var message = getString(R.string.api_error_message)
                var btnText = getString(R.string.ok)
                showAlertDialog(title, message, btnText)
            }

        })

    }

    fun getProductById(productId: Int) {

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

                    if (product?.image_url1 == null) {
                        ivProductImage.setImageResource(R.drawable.product_without_image)
                    } else {
                        val decodedString: ByteArray =
                            Base64.decode(product?.image_url1, Base64.DEFAULT)
                        val decodedByte =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        ivProductImage.setImageBitmap(decodedByte)
                    }

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
                var title = getString(R.string.attention)
                var message = getString(R.string.api_error_message)
                var btnText = getString(R.string.ok)
                showAlertDialog(title, message, btnText)
            }

        })

    }

    fun getSellerById(idSeller: Int) {

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
                var title = getString(R.string.attention)
                var message = getString(R.string.api_error_message)
                var btnText = getString(R.string.ok)
                showAlertDialog(title, message, btnText)
            }

        })
    }

    fun createSubscription() {

        var restockNotification = RestockNotification(
            null,
            10,
            productId
        )

        val createSubscription =
            EventManagerRestockService.getInstance().createSubscription(restockNotification)

        createSubscription.enqueue(object : Callback<RestockNotification> {
            override fun onResponse(
                call: Call<RestockNotification>,
                response: Response<RestockNotification>
            ) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Quando chegarem novos produtos, notificaremos via e-mail.", Toast.LENGTH_SHORT).show()
                    var title = "Notificação via e-mail"
                    var message = "Quando chegarem novos produtos, notificaremos via e-mail."
                    var btnText = "Ok, entendi!"
                    showAlertDialog(title, message, btnText)
                } else {
                    Toast.makeText(context, "Erro: ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<RestockNotification>, t: Throwable) {
                t.printStackTrace()
                var title = getString(R.string.attention)
                var message = getString(R.string.api_error_message)
                var btnText = getString(R.string.ok)
                showAlertDialog(title, message, btnText)
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
         * @return A new instance of fragment ProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}