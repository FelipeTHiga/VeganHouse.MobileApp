package com.example.veganhouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.data.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field

class Catalog : AppCompatActivity() {

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
        setContentView(R.layout.activity_catalog)

        btnFilters = findViewById(R.id.btn_filters)
        containerFilters = findViewById(R.id.container_filters)
        containerCategoryFilters = findViewById(R.id.container_category_filters)
        containerOrderbyFilters = findViewById(R.id.container_orderby_filters)
        spinnerCategory = findViewById(R.id.spinner_category)
        spinnerOrderby = findViewById(R.id.spinner_orderBy)
        progressBar = findViewById(R.id.progress_bar)
        categoryPosition = intent.getIntExtra("categoryPosition", 0)
        categoryValue = intent.getStringExtra("categoryValue").toString()

        val product_card = findViewById<RecyclerView>(R.id.products_component)
        product_card.adapter = adapter

        if (categoryValue == "Todos") this.getAllProducts() else this.getProductByCategory()

        this.setupCategorySpinner()
        this.setupOrderbySpinner()
        this.limitDropDownHeight(spinnerCategory)

    }

    private fun setupOrderbySpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this@Catalog,
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
            this@Catalog,
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
                        Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(baseContext, "Sem produtos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
            }
        })
    }

}