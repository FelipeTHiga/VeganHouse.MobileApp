package com.example.veganhouse.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R
import com.example.veganhouse.service.SessionService
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserLogin
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
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var preferences: SharedPreferences

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
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        val btnSignin: Button = v.findViewById(R.id.btn_signin)
        val btnLogin: Button = v.findViewById(R.id.btn_login)
        etEmail = v.findViewById(R.id.et_email)
        etPassword = v.findViewById(R.id.et_password)
        preferences = activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!

        btnSignin.setOnClickListener {
            val signinFragment = SigninFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fl_wrapper,signinFragment)
            transaction.commit()
        }

        btnLogin.setOnClickListener {
            login(v)
        }

        return v
    }

    fun login(v: View){
        val userLogin = UserLogin (etEmail.text.toString(), etPassword.text.toString())
        val loginUser = SessionService.getInstace().postLogin(userLogin);

        loginUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    redirectHome(v)
                    //setar o atributo da activty userFragment() = ProfileFragment()
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

    fun redirectHome(v:View){
        val homeFragment = HomeFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper,homeFragment)
        Toast.makeText(activity?.baseContext, "Login realizado!", Toast.LENGTH_SHORT).show()
        transaction.commit()
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