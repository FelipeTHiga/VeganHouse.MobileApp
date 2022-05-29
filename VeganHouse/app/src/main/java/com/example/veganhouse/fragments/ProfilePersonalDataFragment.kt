package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.veganhouse.R
import com.example.veganhouse.model.User
import com.example.veganhouse.service.UserService
import com.example.veganhouse.utils.MaskCpf
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfilePersonalDataFragment : Fragment() {

    lateinit var etName: EditText
    lateinit var etSurname: EditText
    lateinit var etEmail: TextInputEditText
    lateinit var etCpf: EditText
    lateinit var btnSave: Button
    lateinit var userData: User
    lateinit var preferences: SharedPreferences
    var loggedUserId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile_personal_data, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        preferences = activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!
        loggedUserId = preferences.getInt("id", 0)

        getUserById(loggedUserId)

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        etName = v.findViewById(R.id.et_name)
        etSurname = v.findViewById(R.id.et_surname)
        etEmail = v.findViewById(R.id.et_email)
        etCpf = v.findViewById(R.id.et_cpf)
        btnSave = v.findViewById(R.id.btn_save)

        var listBtnProfile: ArrayList<ImageButton> = arrayListOf(
            v.findViewById(R.id.btn_profile_data),
            v.findViewById(R.id.btn_profile_orders),
            v.findViewById(R.id.btn_profile_adress)
        )

        listBtnProfile.forEach { btn ->
            btn.setOnClickListener {
                when (btn.id) {
                    R.id.btn_profile_data -> transaction.replace(R.id.fl_wrapper, ProfilePersonalDataFragment::class.java, arguments)
                    R.id.btn_profile_orders ->  transaction.replace(R.id.fl_wrapper, ProfileOrdersFragment::class.java, arguments)
                    R.id.btn_profile_adress -> transaction.replace(R.id.fl_wrapper, ProfileAdressDataFragment::class.java, arguments)
                }
                transaction.commit()
            }
        }

        btnSave.setOnClickListener{
            putUser(loggedUserId)
        }

    }

    private fun getUserById(idUser: Int) {

        val getUserById = UserService.getInstance().getUserById(idUser)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        getUserById.enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful) {

                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(context, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                        return
                    }

                    var user = response.body()

                    userData = User(
                        user?.id,
                        user?.nameUser,
                        user?.surName,
                        user?.cpf,
                        user?.email,
                        user?.passwordUser,
                        user?.isSeller,
                        user?.authenticated,
                        null
                    )

                    var cep = user?.cpf
                    var cepMask = MaskCpf.mask(cep.toString())

                    etName.setText(user?.nameUser)
                    etSurname.setText(user?.surName)
                    etEmail.setText(user?.email)
                    etCpf.setText(cepMask)

                } else {
                    Toast.makeText(context, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
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

    }

    private fun putUser(idUser: Int) {

        var userUpdate = User(
            userData.id,
            userData.nameUser,
            userData.surName,
            userData.cpf,
            etEmail.text.toString(),
            userData.passwordUser,
            userData.isSeller,
            userData.authenticated,
            null
        )

        val putUser = UserService.getInstance().putUser(userUpdate)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        putUser.enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle("Dados do usuário atualizados com sucesso!")
                        .setCancelable(true)
                        .setPositiveButton("Ok") { dialog, _ ->
                            getUserById(idUser)
                        }.show()

                } else {
                    dialogBuilder
                        .setTitle("Erro ao atualizar dados do usuário")
                        .setCancelable(true)
                        .setPositiveButton("Ok, entendi") { dialog, _ ->
                            getUserById(idUser)
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

    }

}