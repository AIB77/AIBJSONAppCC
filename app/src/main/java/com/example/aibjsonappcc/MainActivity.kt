package com.example.aibjsonappcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity()
{
    private var curencyDetails: Datum? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val UserInput = findViewById<View>(R.id.userinput) as EditText
        val ConvrtBTN = findViewById<View>(R.id.btn) as Button
        val spinner = findViewById<View>(R.id.spr) as Spinner

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null)
        {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                )
                {
                    selected = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
        ConvrtBTN.setOnClickListener {
            if(UserInput.text.isNotBlank())
            {
                var UserAmount = UserInput.text.toString()
                var currency: Double = UserAmount.toDouble()

                getCurrency(onResult = {
                    curencyDetails = it

                    when (selected) {
                        0 -> display(calc(curencyDetails?.eur?.inr?.toDouble(), currency));
                        1 -> display(calc(curencyDetails?.eur?.usd?.toDouble(), currency));
                        2 -> display(calc(curencyDetails?.eur?.aud?.toDouble(), currency));
                        3 -> display(calc(curencyDetails?.eur?.sar?.toDouble(), currency));
                        4 -> display(calc(curencyDetails?.eur?.cny?.toDouble(), currency));
                        5 -> display(calc(curencyDetails?.eur?.jpy?.toDouble(), currency));
                    }
                })
            }else
            {
              Toast.makeText(this,"Enter The Money Amount",Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun display(calc: Double)
    {

        val responseText = findViewById<View>(R.id.textView3) as TextView
        responseText.text = "The Result " + calc
    }

    private fun calc(i: Double?, sel: Double): Double
    {
        var M = 0.0
        if (i != null)
        {
            M = (i * sel)
        }
        return M
    }

    private fun getCurrency(onResult: (Datum?) -> Unit)
    {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<Datum> {
                override fun onResponse(
                    call: Call<Datum>,
                    response: Response<Datum>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<Datum>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}