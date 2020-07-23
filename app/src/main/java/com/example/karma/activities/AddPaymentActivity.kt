package com.example.karma.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_payment.*

class AddPaymentActivity : BaseActivity() {
    companion object {
        val bankAdded = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)
        btn_back_payment.setOnClickListener {
            onBackPressed()
        }
        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))

        mbAddCard.setOnClickListener {
            startActivityForResult(Intent(this, NewCardWebViewActivity::class.java), bankAdded)
        }
        btnAddBank.setOnClickListener {
            if (!PreferenceManager.getPlsk(this)?.equals("")!!) {
                startActivityForResult(Intent(this, AddBankActivity::class.java), bankAdded)
            } else {
                Toast.makeText(
                    this@AddPaymentActivity,
                    "Currently, we don't provide this payment method.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val mainIntent = Intent(this@AddPaymentActivity, PaymentCardListActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == bankAdded && resultCode == Activity.RESULT_OK) {
            finish()
        }

    }
}