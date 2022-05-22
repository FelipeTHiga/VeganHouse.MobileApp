package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserLogin
import com.example.veganhouse.service.SessionService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

    lateinit var etEmail: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var etEmailContainer: TextInputLayout
    lateinit var etPasswordContainer: TextInputLayout
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        etEmailContainer = v.findViewById(R.id.container_et_email)
        etPasswordContainer = v.findViewById(R.id.container_et_password)
        preferences =
            activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!

        btnSignin.setOnClickListener {
            val signinFragment = SigninFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fl_wrapper, signinFragment)
            transaction.commit()
        }

        btnLogin.setOnClickListener {
            submitLogin()
        }

        emailFocusListener()
        passwordFocusListener()

        return v
    }

    private fun emailFocusListener() {
        etEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etEmailContainer.helperText = this.validEmail()
            }
        }
    }

    private fun validEmail(): String? {

        var email = etEmail.text.toString()

        if (email.length <= 0) {
            return "Campo obrigatório *"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "E-mail inválido"
        }
        return null
    }

    private fun passwordFocusListener() {
        etPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etPasswordContainer.helperText = this.validPassword()
            }
        }
    }

    private fun validPassword(): String? {

        var password = etPassword.text.toString()

        if (password.length < 6) {
            return "Mínimo de 6 caracteres"
        }
        return null
    }

    private fun submitLogin() {

        etEmailContainer.helperText = validEmail()
        etPasswordContainer.helperText = validPassword()

        val validEmail = etEmailContainer.helperText == null
        val validPassword = etPasswordContainer.helperText == null

        if (validEmail && validPassword) login() else invalidForm()

    }

    private fun resetForm() {

        etEmail.text = null
        etPassword.text = null

        etEmailContainer.helperText = getString(R.string.required)
        etPasswordContainer.helperText = getString(R.string.required)

    }


    private fun invalidForm() {

        val dialogBuilder = android.app.AlertDialog.Builder(context)
        var message = "\nPreencha os campos corretamente!"

        if (etEmailContainer.helperText != null) {
            message += "\n\n E-mail: " + etEmailContainer.helperText
        }
        if (etPasswordContainer.helperText != null) {
            message += "\n\n Senha: " + etPasswordContainer.helperText
        }

        dialogBuilder
            .setTitle("Login inválido")
            .setMessage(message)
            .setPositiveButton("Ok, entendi!") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    fun login() {

        val userLogin = UserLogin(etEmail.text.toString(), etPassword.text.toString())
        val loginUser = SessionService.getInstace().postLogin(userLogin);

        val dialogBuilder = android.app.AlertDialog.Builder(context)

        loginUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle("Login realizado com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("Ir para 'Meu perfil'") { _, _ ->
                            redirectProfileFragment()
                        }.show()

                } else {
                    dialogBuilder
                        .setTitle("Erro ao realizar o login")
                        .setCancelable(true)
                        .setPositiveButton("Ok, entendi!") { dialog, _ ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                dialogBuilder
                    .setTitle("Atenção")
                    .setMessage("Sistema indisponível no momento. Por favor, tente mais tarde.")
                    .setCancelable(true)
                    .setPositiveButton(
                        "Ok, entendi!",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        }).show()
            }
        })

        resetForm()

    }

    fun redirectProfileFragment() {
        val profileFragment = ProfilePersonalData()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, profileFragment)
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