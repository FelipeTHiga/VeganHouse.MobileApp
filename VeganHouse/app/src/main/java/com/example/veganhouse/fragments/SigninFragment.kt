package com.example.veganhouse.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.Login
import com.example.veganhouse.R
import com.example.veganhouse.api.ApiUser
import com.example.veganhouse.data.User
import com.example.veganhouse.data.UserRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SigninFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SigninFragment : Fragment() {
    lateinit var etName: EditText
    lateinit var etSurname: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etCpf: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_signin, container, false)
        val btnLogin: Button = v.findViewById(R.id.btn_signin)

        etName = v.findViewById(R.id.et_name)
        etSurname = v.findViewById(R.id.et_surname)
        etEmail = v.findViewById(R.id.et_email)
        etPassword = v.findViewById(R.id.et_password)
        etCpf = v.findViewById(R.id.et_cpf)

        btnLogin.setOnClickListener {
            registerUser(v)
        }

        return v
    }

    fun registerUser(v:View) {
        var newUser = UserRegister(
            etName.text.toString(),
            etSurname.text.toString(),
            etCpf.text.toString(),
            etEmail.text.toString(),
            etPassword.text.toString(),
            false
        )

        val apiRegister = ApiUser.criar().resgiterUser(newUser)

        apiRegister.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    redirectLogin(v)
                } else {
                    Toast.makeText(activity?.baseContext, "Erro tentar realizar o cadastro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(activity?.baseContext, "Erro na API", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun redirectLogin(v:View){
        val loginFragment = LoginFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper,loginFragment)
        Toast.makeText(activity?.baseContext, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
        transaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SigninFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SigninFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}