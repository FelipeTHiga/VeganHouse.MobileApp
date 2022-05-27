package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserRegister
import com.example.veganhouse.service.UserService
import com.example.veganhouse.utils.MaskCpf
import com.example.veganhouse.utils.ValidCpf
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninFragment : Fragment() {

    lateinit var etName: TextInputEditText
    lateinit var etSurname: TextInputEditText
    lateinit var etEmail: TextInputEditText
    lateinit var etCpf: TextInputEditText
    lateinit var etPassword: TextInputEditText

    lateinit var etNameContainer: TextInputLayout
    lateinit var etSurnameContainer: TextInputLayout
    lateinit var etEmailContainer: TextInputLayout
    lateinit var etCpfContainer: TextInputLayout
    lateinit var etPasswordContainer: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_signin, container, false)
        val btnSignIn: Button = v.findViewById(R.id.btn_signin)

        etName = v.findViewById(R.id.et_name)
        etSurname = v.findViewById(R.id.et_surname)
        etEmail = v.findViewById(R.id.et_email)
        etCpf = v.findViewById(R.id.et_cpf)
        etPassword = v.findViewById(R.id.et_password)

        etNameContainer = v.findViewById(R.id.container_et_name)
        etSurnameContainer = v.findViewById(R.id.container_et_surname)
        etEmailContainer = v.findViewById(R.id.container_et_email)
        etCpfContainer = v.findViewById(R.id.container_et_cpf)
        etPasswordContainer = v.findViewById(R.id.container_et_password)

        etCpf.addTextChangedListener(MaskCpf.mask("###.###.###-##", etCpf))

        btnSignIn.setOnClickListener {
            submitForm()
        }

        nameFocusListener()
        surnameFocusListener()
        emailFocusListener()
        cpfFocusListener()
        passwordFocusListener()

        return v
    }

    private fun nameFocusListener() {
        etName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etNameContainer.helperText = this.validName()
            }
        }
    }

    private fun validName(): String? {

        var name = etName.text.toString()

        if (name.length <= 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun surnameFocusListener() {
        etSurname.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etSurnameContainer.helperText = this.validSurname()
            }
        }
    }

    private fun validSurname(): String? {

        var surname = etSurname.text.toString()

        if (surname.length <= 0) {
            return "Campo obrigatório *"
        }
        return null
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

    private fun cpfFocusListener() {
        etCpf.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etCpfContainer.helperText = this.validCpf()
            }
        }
    }

    private fun validCpf(): String? {

        var cpf = etCpf.text.toString()

        if (cpf.length <= 0) {
            return "Campo obrigatório *"
        } else if (!ValidCpf.myValidateCPF(cpf)) {
            return "CPF inválido"
        } else {
            return null
        }

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

        if (password.length == 0) {
            return "Campo obrigatório *"
        } else if (password.length < 6) {
            return "Mínimo de 6 caracteres"
        }
        return null
    }

    private fun submitForm() {

        etNameContainer.helperText = validName()
        etSurnameContainer.helperText = validSurname()
        etEmailContainer.helperText = validEmail()
        etCpfContainer.helperText = validCpf()
        etPasswordContainer.helperText = validPassword()

        val validName = etNameContainer.helperText == null
        val validSurname = etSurnameContainer.helperText == null
        val validEmail = etEmailContainer.helperText == null
        val validCpf = etCpfContainer.helperText == null
        val validPassword = etPasswordContainer.helperText == null

        if (validName && validSurname && validEmail && validCpf && validPassword) registerUser() else invalidForm()

    }

    private fun resetForm() {

        etName.text = null
        etSurname.text = null
        etEmail.text = null
        etCpf.text = null
        etPassword.text = null

        etNameContainer.helperText = getString(R.string.required)
        etSurnameContainer.helperText = getString(R.string.required)
        etEmailContainer.helperText = getString(R.string.required)
        etCpfContainer.helperText = getString(R.string.required)
        etPasswordContainer.helperText = getString(R.string.required)

    }

    private fun invalidForm() {

        val dialogBuilder = android.app.AlertDialog.Builder(context)
        var message = "\nPreencha os campos corretamente!"

        if (etNameContainer.helperText != null) {
            message += "\n\n Nome: " + etNameContainer.helperText
        }
        if (etSurnameContainer.helperText != null) {
            message += "\n\n Sobrenome: " + etSurnameContainer.helperText
        }
        if (etEmailContainer.helperText != null) {
            message += "\n\n E-mail: " + etEmailContainer.helperText
        }
        if (etCpfContainer.helperText != null) {
            message += "\n\n CPF: " + etCpfContainer.helperText
        }
        if (etPasswordContainer.helperText != null) {
            message += "\n\n Senha: " + etPasswordContainer.helperText
        }

        dialogBuilder
            .setTitle("Cadastro inválido")
            .setMessage(message)
            .setPositiveButton("Ok, entendi") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    fun registerUser() {
        var newUser = UserRegister(
            etName.text.toString(),
            etSurname.text.toString(),
            etCpf.text.toString(),
            etEmail.text.toString(),
            etPassword.text.toString(),
            false
        )

        val apiRegister = UserService.getInstance().resgiterUser(newUser)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        apiRegister.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle("Cadastro realizado com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("Ir para Login") { _, _ ->
                            redirectLogin()
                        }.show()

                } else {
                    dialogBuilder
                        .setTitle("Erro ao realizar o cadastro")
                        .setCancelable(true)
                        .setPositiveButton("Ok, entendi") { dialog, _ ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                dialogBuilder
                    .setTitle("Atenção")
                    .setMessage("Sistema indisponível no momento. Por favor, tente mais tarde.")
                    .setCancelable(true)
                    .setPositiveButton(
                        "Ok, entendi",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        }).show()
            }
        })
        resetForm()
    }

    fun redirectLogin() {
        val loginFragment = LoginFragment()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fl_wrapper, loginFragment)
        transaction.commit()
    }

}