package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.ProductService
import com.example.veganhouse.R
import com.example.veganhouse.adapter.FeaturedProductAdapter
import com.example.veganhouse.adapter.NewProductAdapter
import com.example.veganhouse.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var searchBar: EditText
    lateinit var newproductCard: RecyclerView
    //lateinit var featuredProductCard: RecyclerView
    lateinit var tvDefaultMessage: TextView
    lateinit var tvDefaultMessage2: TextView

    var productSearched = ""
    var arrayNewProducts: ArrayList<Product> = arrayListOf()
    var arrayFeaturedProduct: ArrayList<Product> = arrayListOf()
    var adapterNewProducts = NewProductAdapter(arrayNewProducts, this)
    var adapterFeaturedProduct = FeaturedProductAdapter(arrayFeaturedProduct, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        searchBar = v.findViewById(R.id.search_bar)
        tvDefaultMessage = v.findViewById(R.id.tv_default_message)
        tvDefaultMessage2 = v.findViewById(R.id.tv_default_message_2)

        newproductCard = v.findViewById(R.id.new_products_component)
        newproductCard.adapter = adapterNewProducts

        //featuredProductCard = v.findViewById(R.id.featured_product_component)
        //featuredProductCard.adapter = adapterFeaturedProduct

        var listBtnCategory: ArrayList<ImageButton> = arrayListOf(
            v.findViewById(R.id.btn_acessories),
            v.findViewById(R.id.btn_clothes),
            v.findViewById(R.id.btn_cosmetics),
            v.findViewById(R.id.btn_food),
            v.findViewById(R.id.btn_health),
            v.findViewById(R.id.btn_explore)
        )

        listBtnCategory.forEach { btnProduct ->
            btnProduct.setOnClickListener {
                this.redirectCatalog(it, "")
            }
        }

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                productSearched = searchBar.text.toString()
                // hideKeyboard
                searchBar.clearFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchBar.getApplicationWindowToken(), 0)
                this.redirectCatalog(searchBar, productSearched)
                searchBar.setText("")
            }
            true
        }

        this.getNewProducts()
        this.getFeaturedProduct()

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

    fun redirectCatalog(button: View, productSearched: String) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        if (productSearched != "") {
            arguments.putString("productSearched", productSearched)
        } else {
            var categoryPosition = 0
            var categoryValue = ""

            when (button.id) {
                R.id.btn_acessories -> {
                    categoryPosition = 1
                    categoryValue = "Acessórios"
                }
                R.id.btn_food -> {
                    categoryPosition = 2
                    categoryValue = "Alimentos"
                }
                R.id.btn_cosmetics -> {
                    categoryPosition = 3
                    categoryValue = "Cosméticos"
                }
                R.id.btn_health -> {
                    categoryPosition = 4
                    categoryValue = "Saúde"
                }
                R.id.btn_clothes -> {
                    categoryPosition = 5
                    categoryValue = "Vestimenta"
                }
                R.id.btn_explore -> {
                    categoryPosition = 6
                    categoryValue = "Todos"
                }
            }
            arguments.putInt("categoryPosition", categoryPosition)
            arguments.putString("categoryValue", categoryValue)
        }

        transaction.replace(R.id.fl_wrapper, CatalogFragment::class.java, arguments)
        transaction.commit()

    }

    private fun getNewProducts() {

        val getNewProducts = ProductService.getInstance().getNewProducts()

        getNewProducts.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        tvDefaultMessage.text = getString(R.string.no_result_found)
                        return
                    }
                    if (arrayNewProducts.isNotEmpty()) arrayNewProducts.clear()
                    response.body()?.forEach { product ->
                        arrayNewProducts.add(product)
                    }
                    adapterNewProducts.notifyDataSetChanged()
                    tvDefaultMessage.text = ""
                } else {
                    tvDefaultMessage.text = getString(R.string.no_result_found)
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }
        })
    }

    private fun getFeaturedProduct() {

        val getFeaturedProduct = ProductService.getInstance().getFeaturedProduct()

        getFeaturedProduct.enqueue(object : Callback<Product> {

            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        tvDefaultMessage2.text = getString(R.string.no_result_found)
                        return
                    }
                    var product = response.body()
                    if (arrayFeaturedProduct.isNotEmpty()) arrayFeaturedProduct.clear()

                    if (product != null) {
                        arrayFeaturedProduct.add(product)
                    }
                    adapterFeaturedProduct.notifyDataSetChanged()
                    tvDefaultMessage2.text = ""
                } else {
                    tvDefaultMessage2.text = getString(R.string.no_result_found)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                t.printStackTrace()
                showAlertDialog()
            }
        })
    }

}