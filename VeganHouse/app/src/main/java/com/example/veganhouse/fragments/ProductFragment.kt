package com.example.veganhouse.fragments

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.CertificationItemAdapter
import com.example.veganhouse.adapter.ProductCardAdapter
import com.example.veganhouse.R
import com.example.veganhouse.api.ApiSellerCertified
import com.example.veganhouse.model.Certification
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
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var arrayCertification: ArrayList<Certification> = arrayListOf()
    private var adapter = CertificationItemAdapter(arrayCertification)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        this.getSellerCertified()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_product, container, false)

        val btnOpenContainerCertifications: ImageView =
            v.findViewById(R.id.arrow_container_certifications)
        val btnOpenContainerSeller: ImageView = v.findViewById(R.id.arrow_container_seller)
        val btnRedirectCart: Button = v.findViewById(R.id.btn_add_cart)

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

        return v
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

    fun getSellerCertified() {

        val getSellerCertified = ApiSellerCertified.criar().getSellerCertified(1)
        //val homedateList: List<HomeDate> = gson.fromJson(body, Array<HomeDate>::class.java).toList()

        getSellerCertified.enqueue(object : Callback<List<Certification>> {

            override fun onResponse(
                call: Call<List<Certification>>,
                response: Response<List<Certification>>
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

            override fun onFailure(call: Call<List<Certification>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erro na API", Toast.LENGTH_SHORT).show()
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