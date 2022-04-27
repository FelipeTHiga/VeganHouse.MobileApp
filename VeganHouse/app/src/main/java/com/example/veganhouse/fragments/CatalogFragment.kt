package com.example.veganhouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.ApiProduct
import com.example.veganhouse.ProductCardAdapter
import com.example.veganhouse.R
import com.example.veganhouse.data.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CatalogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatalogFragment : Fragment() {

    lateinit var btnFilters: ImageButton
    lateinit var containerFilters: View
    lateinit var containerCategoryFilters: LinearLayout
    lateinit var containerOrderbyFilters: LinearLayout
    lateinit var spinnerCategory: Spinner
    lateinit var spinnerOrderby: Spinner
    lateinit var progressBar: ProgressBar

    var categoryPosition = 0
    var categoryValue = ""
    var spinnerCategorySelected = ""
    var arrayProduct: ArrayList<Product> = arrayListOf()
    var adapter = ProductCardAdapter(arrayProduct)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_catalog, container, false)

        btnFilters = v.findViewById(R.id.btn_filters)
        containerFilters = v.findViewById(R.id.container_filters)
        containerCategoryFilters = v.findViewById(R.id.container_category_filters)
        containerOrderbyFilters = v.findViewById(R.id.container_orderby_filters)
        spinnerCategory = v.findViewById(R.id.spinner_category)
        spinnerOrderby = v.findViewById(R.id.spinner_orderBy)
        progressBar = v.findViewById(R.id.progress_bar)

        categoryPosition = arguments?.getInt("categoryPosition")!!
        categoryValue = arguments?.getString("categoryValue").toString()

        val product_card = v.findViewById<RecyclerView>(R.id.products_component)
        product_card.adapter = adapter

        if (categoryValue == "Todos") this.getAllProducts() else this.getProductByCategory()

        this.setupCategorySpinner()
        this.setupOrderbySpinner()
        this.limitDropDownHeight(spinnerCategory)

        Toast.makeText(context, "$categoryValue", Toast.LENGTH_SHORT).show()

        return v
    }

    private fun setupOrderbySpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.order_by,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrderby.adapter = adapter

        spinnerOrderby.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position > 0) {
                    var selectedItem = parent!!.getItemAtPosition(position)
                    selectedItem = when {
                        position == 1 -> "lowest-price"
                        position == 2 -> "highest-price"
                        else -> "name"
                    }
                    getProductOrderBy(selectedItem.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    private fun setupCategorySpinner() {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position > 0){
                    val selectedItem = parent!!.getItemAtPosition(position)
                    spinnerCategorySelected = selectedItem.toString()
                    getProductByCategory(spinnerCategorySelected)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun showContainerFilters(v: View) {
        if (containerFilters.visibility == View.GONE) {
            containerFilters.visibility = View.VISIBLE
            containerCategoryFilters.visibility = View.VISIBLE
            containerOrderbyFilters.visibility = View.VISIBLE
        } else {
            containerFilters.visibility = View.GONE
            containerCategoryFilters.visibility = View.GONE
            containerOrderbyFilters.visibility = View.GONE
        }

    }

    fun limitDropDownHeight(spinnerCategory: Spinner) {
        val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true

        val popupWindow: ListPopupWindow = popup.get(spinnerCategory) as ListPopupWindow
        popupWindow.height = (5 * resources.displayMetrics.density).toInt()
    }

    fun getAllProducts() {

        val getAllProducts = ApiProduct.criar().getAllProducts()
        progressBar.visibility = View.VISIBLE

        getAllProducts.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun getProductByCategory() {

        var category = categoryValue
        val getProductByCategory = ApiProduct.criar().getProductByCategory(category)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getProductByCategory(button: View) {

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

        val getProductByCategory = ApiProduct.criar().getProductByCategory(categoryValue)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getProductByCategory(spinnerCategory: String) {

        var category = spinnerCategory
        val getProductByCategory = ApiProduct.criar().getProductByCategory(category)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getProductOrderBy(orderBy: String) {
        var orderBy = orderBy
        var category = spinnerCategorySelected
        //var category = if (spinnerCategorySelected != "name") spinnerCategorySelected else "Todos"
        val getProductOrderBy = ApiProduct.criar().getProductOrderBy(orderBy, category)

        progressBar.visibility = View.VISIBLE

        getProductOrderBy.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })
    }


}