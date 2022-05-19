package com.example.veganhouse.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.*
import com.example.veganhouse.adapter.ProductCardAdapter
import com.example.veganhouse.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CatalogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatalogFragment() : Fragment(), ProductCardAdapter.OnItemClickListener {

    lateinit var btnFilters: ImageButton
    lateinit var containerFilters: View
    lateinit var containerCategoryFilters: LinearLayout
    lateinit var containerOrderbyFilters: LinearLayout
    lateinit var spinnerCategory: Spinner
    lateinit var spinnerOrderby: Spinner
    lateinit var progressBar: ProgressBar
    lateinit var productCard: RecyclerView
    lateinit var btnScroll: ImageView
    lateinit var searchBar: EditText

    var categoryPosition = 1
    var categoryValue = "Todos"
    var productSearched = ""
    var spinnerCategorySelected = ""
    var arrayProduct: ArrayList<Product> = arrayListOf()
    var adapter = ProductCardAdapter(arrayProduct, this)
    var topViewRv = 0
    //var title = getString(R.string.attention)
    //var message = getString(R.string.api_error_message)
    //var btnText = getString(R.string.ok)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_catalog, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        btnFilters = v.findViewById(R.id.btn_filters)
        containerFilters = v.findViewById(R.id.container_filters)
        containerCategoryFilters = v.findViewById(R.id.container_category_filters)
        containerOrderbyFilters = v.findViewById(R.id.container_orderby_filters)
        spinnerCategory = v.findViewById(R.id.spinner_category)
        spinnerOrderby = v.findViewById(R.id.spinner_orderBy)
        progressBar = v.findViewById(R.id.progress_bar)
        btnScroll = v.findViewById(R.id.btn_scroll)
        searchBar = v.findViewById(R.id.search_bar)

        productCard = v.findViewById<RecyclerView>(R.id.products_component)
        productCard.adapter = adapter

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
                getProductByCategory(it)
            }
        }

        btnFilters.setOnClickListener {
            showContainerFilters(it)
        }

        btnScroll.setOnClickListener {
            scrollToTop(it)
        }

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                productSearched = searchBar.text.toString()
                // hideKeyboard
                searchBar.clearFocus()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchBar.getApplicationWindowToken(), 0)
                getProduct()
                searchBar.setText("")
            }
            true
        }

        this.getProduct()
        this.setupCategorySpinner()
        this.setupOrderbySpinner()
        this.limitDropDownHeight(spinnerCategory)

    }

    private fun getProduct() {
        if (productSearched != "") {
            getProductByName(productSearched)
            return
        } else if (arguments != null) {
            productSearched = arguments?.getString("productSearched", "")!!
            categoryPosition = arguments?.getInt("categoryPosition", 1)!!
            categoryValue = arguments?.getString("categoryValue", "Todos")!!
        }
        if (productSearched != "") {
            getProductByName(productSearched)
        } else if (categoryValue == "Todos") {
            getAllProducts()
        } else {
            getProductByCategory()
        }
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
                if (position > 0) {
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
                if (position > 0) {
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

        val getAllProducts = ProductService.getInstance().getAllProducts()
        progressBar.visibility = View.VISIBLE

        getAllProducts.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
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
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })
    }

    fun getProductByCategory() {

        var category = categoryValue
        val getProductByCategory = ProductService.getInstance().getProductByCategory(category)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
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
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog(R.string.attention, "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })

    }

    fun getProductByCategory(button: View) {

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

        val getProductByCategory = ProductService.getInstance().getProductByCategory(categoryValue)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
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
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })
    }

    fun getProductByCategory(spinnerCategory: String) {

        var category = spinnerCategory
        val getProductByCategory = ProductService.getInstance().getProductByCategory(category)

        progressBar.visibility = View.VISIBLE

        getProductByCategory.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
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
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })
    }

    fun getProductOrderBy(orderBy: String) {
        var orderBy = orderBy
        var category = spinnerCategorySelected
        //var category = if (spinnerCategorySelected != "name") spinnerCategorySelected else "Todos"
        val getProductOrderBy = ProductService.getInstance().getProductOrderBy(orderBy, category)

        progressBar.visibility = View.VISIBLE

        getProductOrderBy.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Sem produtos", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
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
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })
    }

    fun getProductByName(productSearched: String) {

        val getProductByName = ProductService.getInstance().getProductByName(productSearched)

        progressBar.visibility = View.VISIBLE

        getProductByName.enqueue(object : Callback<List<Product>> {

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 204 || response.body() == null) {
                        Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        return
                    }

                    var product = response.body()

                    if (arrayProduct.isNotEmpty()) arrayProduct.clear()
                    response.body()?.forEach { product ->
                        arrayProduct.add(product)
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                //showAlertDialog("R.string.attention", "R.string.api_error_message", "R.string.ok")
                //showAlertDialog(title, message, btnText)
                showAlertDialog()
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onItemClick(position: Int) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()
        var productId = arrayProduct[position].id
        var productIsAvailable: Boolean = arrayProduct[position].inventory > 0

        arguments.putInt("productId", productId)
        arguments.putBoolean("productIsAvailable", productIsAvailable)

        transaction.replace(R.id.fl_wrapper, ProductFragment::class.java, arguments)
        transaction.commit()

    }

    fun scrollToTop(btn: View) {
        productCard.smoothScrollToPosition(topViewRv)
        //productCard.smoothScrollBy(topViewRv, topViewRv, null, 5000)
    }


}