package com.example.veganhouse.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.veganhouse.R
import com.example.veganhouse.model.Order
import com.example.veganhouse.service.OrderService
import com.example.veganhouse.utils.ValidCpf
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment : Fragment() {

    lateinit var etCardNumber: TextInputEditText
    lateinit var etExpireDate: TextInputEditText
    lateinit var etCvv: TextInputEditText
    lateinit var etNameOnCard: TextInputEditText

    lateinit var etCardNumberContainer: TextInputLayout
    lateinit var etExpireDateContainer: TextInputLayout
    lateinit var etCvvContainer: TextInputLayout
    lateinit var etNameOnCardContainer: TextInputLayout

    lateinit var totalValue: TextView
    lateinit var preferences: SharedPreferences
    var idUser: Int = 0

    //lateinit var listOrder : List<com.example.veganhouse.model.Order>
    lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_payment, container, false)

        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        preferences =
            activity?.baseContext?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)!!
        idUser = preferences.getInt("id", 0)

        getOrders()

        totalValue = v.findViewById(R.id.tv_total_payment)
        val btnPaymentResult: Button = v.findViewById(R.id.btn_payment_result)
        var total = arguments?.getDouble("totalCart", 0.0)
        totalValue.text = "R$ %.2f".format(total)

        etCardNumber = v.findViewById(R.id.et_card_number)
        etExpireDate = v.findViewById(R.id.et_expire_date)
        etCvv = v.findViewById(R.id.et_cvv)
        etNameOnCard = v.findViewById(R.id.et_name_on_card)

        etCardNumberContainer = v.findViewById(R.id.container_et_card_number)
        etExpireDateContainer = v.findViewById(R.id.container_et_expire_date)
        etCvvContainer = v.findViewById(R.id.container_et_cvv)
        etNameOnCardContainer = v.findViewById(R.id.container_et_name_on_card)

        btnPaymentResult.setOnClickListener {
            aprovedPayment()
        }

        cardNumberListener()
        expireDateFocusListener()
        cvvFocusListener()
        nameOnCardFocusListener()

    }

    private fun cardNumberListener() {
        etCardNumber.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etCardNumberContainer.helperText = this.validCardNumber()
            }
        }
    }

    private fun validCardNumber(): String? {

        var cardNumber = etCardNumber.text.toString()

        if (cardNumber.length <= 0) {
            return "Campo obrigatório *"
        } else if (cardNumber.length < 12) {
            return "Mínimo de 12 caracteres"
        }
        return null
    }

    private fun expireDateFocusListener() {
        etExpireDate.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etExpireDateContainer.helperText = this.validExpireDate()
            }
        }
    }

    private fun validExpireDate(): String? {

        var expireDate = etExpireDate.text.toString()

        if (expireDate.length <= 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    private fun cvvFocusListener() {
        etCvv.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etCvvContainer.helperText = this.validCvv()
            }
        }
    }

    private fun validCvv(): String? {

        var cvv = etCvv.text.toString()

        if (cvv.length <= 0) {
            return "Campo obrigatório *"
        } else if(cvv.length < 3) {
            return "Mínimo de 3 caracteres"
        }
        return null
    }

    private fun nameOnCardFocusListener() {
        etNameOnCard.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                etNameOnCardContainer.helperText = this.validNameOnCard()
            }
        }
    }

    private fun validNameOnCard(): String? {

        var nameOnCard = etNameOnCard.text.toString()

        if (nameOnCard.length == 0) {
            return "Campo obrigatório *"
        }
        return null
    }

    fun getOrders() {
//        val getUserOrder = OrderService.getInstance().getUserOrder(idUser)
        val getOrder = OrderService.getInstance().getOrder(idUser)

        getOrder.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    order = response.body()!!

                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                t.printStackTrace()
            }
        })


//        getUserOrder.enqueue(object : Callback<List<Order>> {
//            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
//                if (response.isSuccessful){
//
//                    response.body()?.forEach { order ->
//                        listOrder.add(order)
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })

    }

    fun aprovedPayment() {
        val putOrder = OrderService.getInstance().updateOrder(idUser)

        putOrder.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    updateOrdedr(order.idOrder)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    fun updateOrdedr(idOrder: Int) {
        val statusPayment = "Pagamento aprovado"
        val pachOrder = OrderService.getInstance().pacthOrder(statusPayment, idOrder)

        pachOrder.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    redirectPaymentResult()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun statusOrders() {
//        for (item : Order in listOrder) {
//            updateOrdedr(item.idOrder)
//        }
        redirectPaymentResult()
    }

    fun redirectPaymentResult() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        val arguments = Bundle()

        transaction.replace(R.id.fl_wrapper, PaymentResultFragment::class.java, arguments)
        transaction.commit()

    }


}