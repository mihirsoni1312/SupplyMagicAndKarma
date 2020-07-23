package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.interfaces.CardDeleteClickListner
import com.example.karma.responseModel.PaymentCardModel
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.row_payments.view.*


class PaymentListAdapter(
    private val context: Context,
    private var items: ArrayList<PaymentCardModel>,
    private var CardDeleteClickListner: CardDeleteClickListner,
    private var selectPaymentMethod: SelectPaymentMethod


) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_payments,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_lable.text = items[position].Brand
        holder.txt_card_number.text = "**** **** **** " + items[position].Last4

        if (items[position].id.equals("3")) {
            holder.img_delete.visibility = View.GONE
        }

        holder.img_delete.setOnClickListener {
            CardDeleteClickListner.cartDelete(
                items[position].id,
                items[position].Type,
                items[position].BankDetails
            )
        }
        holder.tvDefault.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        if (items[position].isDefault) {
            holder.tvDefault.visibility = View.VISIBLE
            holder.Titleimage.setImageResource(R.drawable.withselect)

        } else {
            holder.tvDefault.visibility = View.GONE
            holder.Titleimage.setImageResource(R.drawable.withoutselect)
        }

        holder.llmain.setOnClickListener {

            for (i in items) {
                if (i.id == items[position].id) {
                    items[position].isDefault = true
                } else {
                    i.isDefault = false
                }
                notifyDataSetChanged()
            }

            selectPaymentMethod.selectedPaymentmethod(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_lable = view.txt_lable!!
        val txt_card_number = view.txt_card_number!!
        val img_delete = view.img_delete!!
        val img_deleteCard = view.img_deleteCard!!
        val Titleimage = view.Titleimage!!
        val llmain = view.llmain!!
        val tvDefault = view.tvDefault!!


    }

    interface SelectPaymentMethod {
        fun selectedPaymentmethod(paymentCardModel: PaymentCardModel)
    }
}