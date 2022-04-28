package com.example.veganhouse.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.Catalog
import com.example.veganhouse.R

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

        val btnProduct: RelativeLayout = v.findViewById(R.id.rl_product)

        btnProduct.setOnClickListener {
            redirectProduct()
        }

        return v
    }

    fun redirectProduct(){
        val productFragment = ProductFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, productFragment)
        transaction.commit()
    }

//    fun telaCatalog(button: View) {
//
//        val bundle = Bundle()
//        val catalogFragment = CatalogFragment()
//        var categoryPosition = 0
//        var categoryValue = ""
//
//        when (button.id) {
//            R.id.btn_acessories -> {
//                categoryPosition = 1
//                categoryValue = "Acessórios"
//            }
//            R.id.btn_food -> {
//                categoryPosition = 2
//                categoryValue = "Alimentos"
//            }
//            R.id.btn_cosmetics -> {
//                categoryPosition = 3
//                categoryValue = "Cosméticos"
//            }
//            R.id.btn_health -> {
//                categoryPosition = 4
//                categoryValue = "Saúde"
//            }
//            R.id.btn_clothes -> {
//                categoryPosition = 5
//                categoryValue = "Vestimenta"
//            }
//            R.id.btn_explore -> {
//                categoryPosition = 6
//                categoryValue = "Todos"
//            }
//        }
//
//        bundle.putInt("categoryPosition", categoryPosition)
//        bundle.putString("categoryValue", categoryValue)
//        catalogFragment.arguments = bundle
//        makeCurrentFragment(catalogFragment)
//
//    }


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