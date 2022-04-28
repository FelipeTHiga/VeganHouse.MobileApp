package com.example.veganhouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R

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
        val v = inflater.inflate(R.layout.fragment_product, container, false)

        val btnOpenMenuRedesSociais: ImageView = v.findViewById(R.id.iconArrow)
        val btnOpenMenuSelos: ImageView = v.findViewById(R.id.iv_menu_selos)
        val btnRedirectCart: Button = v.findViewById(R.id.btn_redirect_cart)

        btnOpenMenuRedesSociais.setOnClickListener {
            openMenuRedesSociais(v)
        }
        btnRedirectCart.setOnClickListener {
            redirectCart()
        }
        btnOpenMenuSelos.setOnClickListener {
            openMenuSelos(v)
        }

        return v
    }

    fun openMenuRedesSociais(v:View){

        var arrow = v.findViewById<ImageView>(R.id.iconArrow)
        var instagram = v.findViewById<ImageView>(R.id.iconInstagram)
        var facebook = v.findViewById<ImageView>(R.id.iconFacebook)
        var whatsapp = v.findViewById<ImageView>(R.id.iconWhatsapp)

        if(instagram.visibility == View.VISIBLE){
            instagram.visibility = View.GONE
            facebook.visibility = View.GONE
            whatsapp.visibility = View.GONE
        } else {
            instagram.visibility = View.VISIBLE
            facebook.visibility = View.VISIBLE
            whatsapp.visibility = View.VISIBLE
        }
    }


    fun openMenuSelos(v:View){

        var arrow = v.findViewById<ImageView>(R.id.iconArrow)

        var containerImage = v.findViewById<RelativeLayout>(R.id.imageContainer)

        if (containerImage.visibility == View.VISIBLE){
            containerImage.visibility = View.GONE
        } else {
            containerImage.visibility = View.VISIBLE
        }

//        var selo1 = findViewById<ImageView>(R.id.selo1)
//        var selo2 = findViewById<ImageView>(R.id.selo2)
//        var selo3 = findViewById<ImageView>(R.id.selo3)
//
//        var textSelo1 = findViewById<TextView>(R.id.textSelo1)
//        var textSelo2 = findViewById<TextView>(R.id.textSelo2)
//        var textSelo3 = findViewById<TextView>(R.id.textSelo3)
//
//        if(selo1.visibility == View.VISIBLE){
//            selo1.visibility = View.GONE
//            selo2.visibility = View.GONE
//            selo3.visibility = View.GONE
//
//            textSelo1.visibility = View.GONE
//            textSelo2.visibility = View.GONE
//            textSelo3.visibility = View.GONE
//
//        } else {
//            selo1.visibility = View.VISIBLE
//            selo2.visibility = View.VISIBLE
//            selo3.visibility = View.VISIBLE
//
//            textSelo1.visibility = View.VISIBLE
//            textSelo2.visibility = View.VISIBLE
//            textSelo3.visibility = View.VISIBLE
//        }

    }

    fun redirectCart(){
        val cartFragment = CartFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, cartFragment)
        transaction.commit()
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