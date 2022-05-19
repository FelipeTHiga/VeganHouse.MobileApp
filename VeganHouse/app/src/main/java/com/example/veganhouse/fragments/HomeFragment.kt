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
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.finishAfterTransition
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.ProductService
import com.example.veganhouse.R
import com.example.veganhouse.adapter.FeaturedProductCardAdapter
import com.example.veganhouse.adapter.NewProductCardAdapter
import com.example.veganhouse.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var searchBar: EditText
    lateinit var newproductCard: RecyclerView
    lateinit var featuredProductCard: RecyclerView

    var productSearched = ""
    var arrayNewProducts: ArrayList<Product> = arrayListOf()
    var arrayFeaturedProduct: ArrayList<Product> = arrayListOf()
    var adapterNewProducts = NewProductCardAdapter(arrayNewProducts, this)
    var adapterFeaturedProduct = FeaturedProductCardAdapter(arrayFeaturedProduct, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        newproductCard = v.findViewById<RecyclerView>(R.id.new_products_component)
        newproductCard.adapter = adapterNewProducts

        featuredProductCard = v.findViewById<RecyclerView>(R.id.featured_product_component)
        featuredProductCard.adapter = adapterFeaturedProduct

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
                redirectCatalog(it, "")
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
                redirectCatalog(searchBar, productSearched)
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
            .setMessage("Sistema indisponível no momento. Por favor, tente mais tarde.")
            .setCancelable(true)
            .setPositiveButton("Ok, entendi!", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Atenção")
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

    fun getNewProducts() {

        val getNewProducts = ProductService.getInstance().getNewProducts()

        getNewProducts.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayNewProducts.isNotEmpty()) arrayNewProducts.clear()
                    response.body()?.forEach { product ->
                        arrayNewProducts.add(product)
                    }
                    adapterNewProducts.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                showAlertDialog()
            }
        })
    }

    fun getFeaturedProduct() {

        val getFeaturedProduct = ProductService.getInstance().getFeaturedProduct()

        getFeaturedProduct.enqueue(object : Callback<Product> {

            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    var product = response.body()
                    if (arrayFeaturedProduct.isNotEmpty()) arrayFeaturedProduct.clear()

                    if (product != null) {
                        arrayFeaturedProduct.add(product)
                    }

                    adapterFeaturedProduct.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                showAlertDialog()
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}