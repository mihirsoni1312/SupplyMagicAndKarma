package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.interfaces.HomeCategoryClickListner
import com.example.karma.responseModel.CategoryModel
import com.example.karma.utils.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_category_item.view.*


class HomeCategoryListAdapter(
    private val context: Context,
    private var items: ArrayList<CategoryModel>,
    private var clickListner: HomeCategoryClickListner


) : RecyclerView.Adapter<HomeCategoryListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_category_item,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_title.text = items[position].name
        holder.txt_title.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        Picasso.get().load("http://207.244.244.64/CategoryImages/" + items[position].image)
            .into(holder.img_product)
        holder.txt_desc.text = items[position].desc
        holder.ll_row_category.setOnClickListener {
            clickListner.getProductListClick(items[position].categoryId, items[position].name)
        }

        holder.card_cat.setOnClickListener {
            clickListner.getProductListClick(items[position].categoryId, items[position].name)
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
        val txt_title = view.txt_title!!
        val txt_desc = view.txt_desc!!
        val img_product = view.img_product
        val card_cat = view.card_cat!!
        val ll_row_category = view.ll_row_category!!


    }

}