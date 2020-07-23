package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.ProductDetailActivity
import com.example.karma.interfaces.SearchToAddToCart
import com.example.karma.responseModel.ProductListModel
import com.example.karma.utils.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_search.view.*


class SearchAdapter(
    private val context: Context,
    private var searchToAddToCart: SearchToAddToCart
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var items: ArrayList<ProductListModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_search,
                parent,
                false
            )
        )
    }

    fun addData(items: ArrayList<ProductListModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_search_title.text = items[position].name
        holder.txt_search_title.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    context
                )
            )
        )
        holder.txt_new_price_search.text =
            "${items[position].priceInfo?.c} ${items[position].priceInfo?.new}"
        holder.txt_old_price_search.text =
            "${items[position].priceInfo?.c} ${items[position].priceInfo?.old}"
        holder.txt_discountpersent.text =
            "Save " + "${items[position].priceInfo?.c} ${items[position].priceInfo!!.da}" +
                    " (" + "${items[position].priceInfo!!.dp}" + "%" + ")"

        holder.txtdescription.text = "${items[position].sdesc}"

        Picasso.get().load("http://207.244.244.64/ItemImages/" + items[position].img)
            .into(holder.img_product_search)

        if (items[position].aI.size == 1) {
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[0].image)
                .into(holder.vagnonvagIcon)
            holder.imgvagnonvagIcon.visibility = View.GONE
        } else if (items[position].aI.size == 2) {
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[0].image)
                .into(holder.vagnonvagIcon)
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[1].image)
                .into(holder.imgvagnonvagIcon)
        } else {
            holder.vagnonvagIcon.visibility = View.GONE
            holder.imgvagnonvagIcon.visibility = View.GONE
        }
        holder.txt_old_price_search.paintFlags =
            holder.txt_old_price_search.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.unavailable.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))

        if (items[position].iAv) {
            holder.unavailable.visibility = View.GONE
        } else {
            holder.unavailable.visibility = View.VISIBLE
        }

        holder.ll_productRow_search.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("pId", "" + items[position]._id)
            context.startActivity(intent)
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


    fun getItemsize(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_search_title = view.txt_itemview!!
        val txt_new_price_search = view.txt_newPrice!!
        val txt_old_price_search = view.txtdiscountPrice!!
        val img_product_search = view.img_product_search!!
        val ll_productRow_search = view.ll_productRow_search!!
        val vagnonvagIcon = view.vagnonvagIcon!!

        //        val ll_content = view.llmain!!
        val ll_addButton = view.ll_addButton
        val imgvagnonvagIcon = view.imgvagnonvagIcon
        val txt_discountpersent = view.txt_discountpersent
        val txtdescription = view.txtdescription
        val unavailable = view.unavailable
    }

    fun clearList() {
        items.clear()
        notifyDataSetChanged()
    }

    init {
        items = ArrayList()
    }
}

