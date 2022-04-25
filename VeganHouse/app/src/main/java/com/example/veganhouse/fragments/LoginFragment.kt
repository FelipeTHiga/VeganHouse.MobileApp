package com.example.veganhouse.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.MainActivity
import com.example.veganhouse.R
import com.example.veganhouse.Signin
import com.example.veganhouse.api.ApiLogin
import com.example.veganhouse.data.User
import com.example.veganhouse.data.UserLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        val btnSignin: Button = v.findViewById(R.id.btn_signin)

        btnSignin.setOnClickListener {
            val signinFragment = SigninFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fl_wrapper,signinFragment)
            transaction.commit()
        }

        return v
    }

    fun login(v: View){
        val userLogin = UserLogin (etEmail.text.toString(), etPassword.text.toString())
        val loginUser = ApiLogin.criar().postLogin(userLogin);


        loginUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    redirectHome(v)
                } else {
                    // etEmail.error = "Email incorreto"
                    // etPassword.error = "Senha incorreta"
                    Toast.makeText(activity?.baseContext, "Erro tentar realizar o login", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity?.baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun redirectSignin(v:View){
        val signin = Intent(this, Signin::class.java)
        startActivity(signin)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}