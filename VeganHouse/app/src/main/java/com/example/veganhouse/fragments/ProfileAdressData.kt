package com.example.veganhouse.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.veganhouse.R
import com.example.veganhouse.model.Adress
import com.example.veganhouse.service.CepService
import com.example.veganhouse.service.UserService
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
 * Use the [ProfileAdressData.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileAdressData : Fragment() {

    lateinit var etCep: TextInputEditText
    lateinit var etState: TextInputEditText
    lateinit var etCity: TextInputEditText
    lateinit var etDistrict: TextInputEditText
    lateinit var etStreet: TextInputEditText
    lateinit var etNumber: TextInputEditText
    lateinit var etComplement: TextInputEditText

    lateinit var etCepContainer: TextInputLayout
    lateinit var etStateContainer: TextInputLayout
    lateinit var etCityContainer: TextInputLayout
    lateinit var etDistrictContainer: TextInputLayout
    lateinit var etStreetContainer: TextInputLayout
    lateinit var etNumberContainer: TextInputLayout
    lateinit var etComplementContainer: TextInputLayout

    lateinit var btnSave: Button
    lateinit var btnUpdate: Button

    var loggedUserId = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile_adress_data, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        getUserAdress(loggedUserId)

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        btnSave = v.findViewById(R.id.btn_save)
        btnUpdate = v.findViewById(R.id.btn_update)

        etCep = v.findViewById(R.id.et_cep)
        etState = v.findViewById(R.id.et_state)
        etCity = v.findViewById(R.id.et_city)
        etDistrict = v.findViewById(R.id.et_district)
        etStreet = v.findViewById(R.id.et_street)
        etNumber = v.findViewById(R.id.et_number)
        etComplement = v.findViewById(R.id.et_complement)

        etCepContainer = v.findViewById(R.id.container_et_cep)
        etStateContainer = v.findViewById(R.id.container_et_state)
        etCityContainer = v.findViewById(R.id.container_et_city)
        etDistrictContainer = v.findViewById(R.id.container_et_district)
        etStreetContainer = v.findViewById(R.id.container_et_street)
        etNumberContainer = v.findViewById(R.id.container_et_number)
        etComplementContainer = v.findViewById(R.id.container_et_complement)

        var listBtnProfile: ArrayList<ImageButton> = arrayListOf(
            v.findViewById(R.id.btn_profile_data),
            v.findViewById(R.id.btn_profile_orders),
            v.findViewById(R.id.btn_profile_adress)
        )

        listBtnProfile.forEach { btn ->
            btn.setOnClickListener {
                when (btn.id) {
                    R.id.btn_profile_data -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfilePersonalData::class.java,
                        arguments
                    )
                    R.id.btn_profile_orders -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfileOrders::class.java,
                        arguments
                    )
                    R.id.btn_profile_adress -> transaction.replace(
                        R.id.fl_wrapper,
                        ProfileAdressData::class.java,
                        arguments
                    )
                }
                transaction.commit()
            }
        }

        btnSave.setOnClickListener {
            submitForm(it)
        }

        btnUpdate.setOnClickListener {
            submitForm(it)
        }

        cepFocusListener()
        stateFocusListener()
        cityFocusListener()
        districtFocusListener()
        streetFocusListener()
        numberFocusListener()
    }

    private fun cepFocusListener() {
        etCep.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etCepContainer.helperText = this.validCep()
            }
        }
    }

    private fun validCep(): String? {

        var cep = etCep.text.toString()

        if (cep.length <= 0) {
            return "Campo obrigatório *"
        } else if (cep.length != 8) {
            return "Cep inválido"
        } else {
            pullCep(cep)
        }
        return null
    }

    private fun pullCep(cep: String) {
//        try {
//            val currentCep = CepService.getCep("123")
//            if(currentCep.erro != null){
//                error.value = true
//                errorType.value = 1
//                loading.value = false
//            }else {
//                cep.value = currentCep
//                error.value = false
//                loading.value = false
//            }
//        }catch (e: Exception){
//            error.value = true
//            errorType.value = 0
//            loading.value = false
//        }
    }


    private fun stateFocusListener() {
        etState.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etStateContainer.helperText = this.validState()
            }
        }
    }

    private fun validState(): String? {

        var state = etState.text.toString()

        if (state.length <= 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun cityFocusListener() {
        etCity.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etCityContainer.helperText = this.validCity()
            }
        }
    }

    private fun validCity(): String? {

        var city = etCity.text.toString()

        if (city.length <= 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun districtFocusListener() {
        etDistrict.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etDistrictContainer.helperText = this.validDistrict()
            }
        }
    }

    private fun validDistrict(): String? {

        var district = etDistrict.text.toString()

        if (district.length <= 0) {
            return "Campo obrigatório *"
        }
        return null


    }

    private fun streetFocusListener() {
        etStreet.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etStreetContainer.helperText = this.validStreet()
            }
        }
    }

    private fun validStreet(): String? {

        var street = etStreet.text.toString()

        if (street.length == 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun numberFocusListener() {
        etNumber.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etNumberContainer.helperText = this.validNumber()
            }
        }
    }

    private fun validNumber(): String? {

        var number = etNumber.text.toString()

        if (number.length == 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun submitForm(button: View) {

        etCepContainer.helperText = validCep()
        etStateContainer.helperText = validState()
        etCityContainer.helperText = validCity()
        etDistrictContainer.helperText = validDistrict()
        etStreetContainer.helperText = validStreet()
        etNumberContainer.helperText = validNumber()

        val validCep = etCepContainer.helperText == null
        val validState = etStateContainer.helperText == null
        val validCity = etCityContainer.helperText == null
        val validDistrict = etDistrictContainer.helperText == null
        val validStreet = etStreetContainer.helperText == null
        val validNumber = etNumberContainer.helperText == null

        if (validCep && validState && validCity && validDistrict && validStreet && validNumber) {
            when(button.id) {
                R.id.btn_save -> createUserAdress()
                R.id.btn_update -> putUserAdress()
            }
        } else invalidForm()

    }

//    private fun resetForm() {
//
//        etNameContainer.helperText = getString(R.string.required)
//        etSurnameContainer.helperText = getString(R.string.required)
//        etEmailContainer.helperText = getString(R.string.required)
//        etCpfContainer.helperText = getString(R.string.required)
//        etPasswordContainer.helperText = getString(R.string.required)
//
//    }

    private fun invalidForm() {

        val dialogBuilder = android.app.AlertDialog.Builder(context)
        var message = "\nPreencha os campos corretamente!"

        if (etCepContainer.helperText != null) {
            message += "\n\n CEP: " + etCepContainer.helperText
        }
        if (etStateContainer.helperText != null) {
            message += "\n\n Estado: " + etStateContainer.helperText
        }
        if (etCityContainer.helperText != null) {
            message += "\n\n Cidade: " + etCityContainer.helperText
        }
        if (etDistrictContainer.helperText != null) {
            message += "\n\n Bairro: " + etDistrictContainer.helperText
        }
        if (etStreetContainer.helperText != null) {
            message += "\n\n Rua: " + etStreetContainer.helperText
        }
        if (etNumberContainer.helperText != null) {
            message += "\n\n Número: " + etNumberContainer.helperText
        }

        dialogBuilder
            .setTitle("Cadastro inválido")
            .setMessage(message)
            .setPositiveButton("Ok, entendi!") { dialog, _ ->
                dialog.cancel()
            }.show()
    }


    private fun getUserAdress(idUser: Int) {

        val getUserAdress = UserService.getInstance().getUserAdress(idUser)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        getUserAdress.enqueue(object : Callback<Adress> {
            override fun onResponse(call: Call<Adress>, response: Response<Adress>) {
                if (response.isSuccessful) {

                    if (response.code() == 404 || response.body() == null) {
                        Toast.makeText(
                            context,
                            "Ususário ainda não possui endereço cadastrado",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    var adress = response.body()

                    etCep.setText(adress?.cep)
                    etState.setText(adress?.state)
                    etCity.setText(adress?.city)
                    etDistrict.setText(adress?.district)
                    etStreet.setText(adress?.street)
                    etNumber.setText(adress?.number)
                    etComplement.setText(adress?.complement)

                    btnSave.visibility = View.GONE
                    btnUpdate.visibility = View.VISIBLE

                } else {
                    Toast.makeText(
                        context,
                        "Ususário ainda não possui endereço cadastrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Adress>, t: Throwable) {
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
    }


    private fun putUserAdress() {

        var adressUpdate = Adress(
            null,
            etStreet.text.toString(),
            etNumber.text.toString(),
            etState.text.toString(),
            etCity.text.toString(),
            etComplement.text.toString(),
            etCep.text.toString(),
            etDistrict.text.toString(),
            loggedUserId
        )

        val putUserAdress = UserService.getInstance().putUserAdress(loggedUserId, adressUpdate)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        putUserAdress.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle("Dados do usuário atualizados com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.cancel()
                        }.show()

                } else {
                    dialogBuilder
                        .setTitle("Erro ao atualizar dados do usuário")
                        .setCancelable(true)
                        .setPositiveButton("Ok, entendi!") { dialog, _ ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
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

    }

    fun createUserAdress() {
        var newAdress = Adress(
            null,
            etStreet.text.toString(),
            etNumber.text.toString(),
            etState.text.toString(),
            etCity.text.toString(),
            etComplement.text.toString(),
            etCep.text.toString(),
            etDistrict.text.toString(),
            loggedUserId
        )

        val createUserAdress = UserService.getInstance().createUserAdress(newAdress)
        val dialogBuilder = android.app.AlertDialog.Builder(context)

        createUserAdress.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                    dialogBuilder
                        .setTitle("Endereço cadastrado com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("Ok, entendi!") { dialog, _ ->
                            getUserAdress(loggedUserId)
                        }.show()

                } else {
                    dialogBuilder
                        .setTitle("Erro ao cadastrar o endereço")
                        .setCancelable(true)
                        .setPositiveButton("Ok, entendi!") { dialog, _ ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
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
    }


    companion object {

    }
}