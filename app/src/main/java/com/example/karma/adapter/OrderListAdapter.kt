package com.example.karma.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.ProductDetailActivity
import com.example.karma.interfaces.OrderItemClickListner
import com.example.karma.response.getOrderListByUserId.ProductList

import kotlinx.android.synthetic.main.row_order.view.*

class OrderListAdapter(
    private val context: Context,
    private var orderItemClickListner: OrderItemClickListner
) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {


    private var items: ArrayList<ProductList> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderListAdapter.ViewHolder {
        return OrderListAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_order,
                parent,
                false
            )
        )
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
        val llmain = view.llmain!!
        val tv_date = view.tv_date!!
        val tv_inv = view.tv_inv!!
        val tv_prise = view.tv_prise!!
        val ivIcon = view.img_select!!

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.llmain.setOnClickListener { orderItemClickListner.OrderItemClick(items[position]) }
        holder.tv_inv.text = items[position].invoiceId.toString()
        holder.tv_prise.text = "$ " + items[position].payableAmount.toString()

        holder.tv_date.text = items[position].orderDate

        if (items[position].status == "0") {
            holder.ivIcon.setImageResource(R.drawable.cancelled_icon)

        }
        if (items[position].status == "1") {
            holder.ivIcon.setImageResource(R.drawable.placed_icon)

        }
        if (items[position].status == "2") {
            holder.ivIcon.setImageResource(R.drawable.processing_icon)


        }
        if (items[position].status == "3") {
            holder.ivIcon.setImageResource(R.drawable.out_for_delivery_icon)

        }
        if (items[position].status == "4") {
            holder.ivIcon.setImageResource(R.drawable.icn_complete)

        }

    }

    fun addDate(items: ArrayList<ProductList>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}
