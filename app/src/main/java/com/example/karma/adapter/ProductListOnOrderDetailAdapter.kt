package com.example.karma.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.ProductDetailActivity
import com.example.karma.responseModel.orderDetailResponse.ProductInfo
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.row_product.view.*

class ProductListOnOrderDetailAdapter(val context: Context, var productInfo: List<ProductInfo>) :
    RecyclerView.Adapter<ProductListOnOrderDetailAdapter.ProductListOnOrderDetailViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListOnOrderDetailViewHolder {

        return ProductListOnOrderDetailViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productInfo.size
    }

    override fun onBindViewHolder(holder: ProductListOnOrderDetailViewHolder, position: Int) {

        val p = productInfo[position]
        holder.tvName.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(context)))
        if (p.iAv!!) {
            holder.tvNotDeliverable.visibility = View.GONE
        } else {
            holder.tvNotDeliverable.setTextColor(
                Color.parseColor(
                    PreferenceManager.getBackgroundColor(
                        context
                    )
                )
            )
            holder.tvNotDeliverable.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("pId", "" + productInfo[position].id)
            context.startActivity(intent)
        }

        holder.tvName.text = p.name
        holder.tvNewPrice.text = "$ "+p.pI?.n
        holder.tvQty.text = "${p.qty} x "
        holder.tvTotal.text = "$ "+p.tot
    }

    class ProductListOnOrderDetailViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNotDeliverable = itemView.tvNotDeliverable!!
        val tvNewPrice = itemView.tvNewPrice!!
        val tvQty = itemView.tvQty!!
        val tvTotal = itemView.tvTotal!!
        val tvName = itemView.tvName!!
    }

}